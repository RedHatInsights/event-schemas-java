const {
  quicktypeMultiFile,
  InputData,
  JSONSchemaInput,
  JSONSchemaStore, JavaTargetLanguage,
} = require("quicktype-core");
const { execSync } = require("child_process");
const fsPromises = require("fs/promises");
const fs = require("fs");
const path = require("path");
const { chdir } = require("process");
const {CloudEventJavaLanguage} = require("./CloudEventJavaLanguage");
const BASE_JAVA_PACKAGE = 'com.redhat.cloud.event';
const BASE_JAVA_SRC_PATH = "src/main/java";

class StaticJSONSchemaStore extends JSONSchemaStore {
  constructor() {
    super();
  }
  async fetch(address) {
    const contents = await fsPromises.readFile(address);
    return JSON.parse(contents);
  }
}

async function generateFiles(repoRoot, subdir) {
  const versions = await fsPromises.readdir(`${repoRoot}/api/schemas/${subdir}`);
  for (const version of versions) {
    const schemas = await fsPromises.readdir(`${repoRoot}/api/schemas/${subdir}/${version}`);
    for (const schema of schemas) {
      if (schema === 'empty.json' && subdir === 'core') continue // skip empty, quicktype generates bad code for this
      const schemaPath = `${repoRoot}/api/schemas/${subdir}/${version}/${schema}`;
      const jsonSchemaString = await fsPromises.readFile(schemaPath, {encoding: 'utf8'});
      const filename = path.basename(schemaPath);
      const schemaInput = new JSONSchemaInput(new StaticJSONSchemaStore());
      const inputData = new InputData();
      await schemaInput.addSource({
        name: filename.replace('.json', ''),
        uris: [schemaPath],
        schema: jsonSchemaString
      });
      inputData.addInput(schemaInput);
      await generateJavaFiles(subdir, version, inputData, schema);
    }
  }
}

async function generateJavaFiles(subdir, version, inputData, _schema) {
  const subPackage = `${subdir.replaceAll('/', '.')}.${version}`;
  const packageName = `${BASE_JAVA_PACKAGE}.${subPackage}`;
  const outputPath = `${BASE_JAVA_SRC_PATH}/${packageName.replaceAll('.', '/')}`;
  const lang = new CloudEventJavaLanguage();
  const result = await quicktypeMultiFile({
    inputData,
    lang,
    rendererOptions: {
      package: packageName,
    },
  });
  await fsPromises.mkdir(outputPath, {recursive: true});
  for (const [filename, contents] of result) {
    await fsPromises.writeFile(`${outputPath}/${filename}`, contents.lines.join('\n'));
  }
}

async function main() {
  const repoRoot = execSync('git rev-parse --show-toplevel', { encoding: 'utf8' }).trim();
  console.info('Clearing out existing source files')
  for (const path of ['apps', 'core']) {
    fs.rmSync(`${repoRoot}/src/main/java/com/redhat/cloud/event/${path}`, {recursive: true, force: true});
  }
  console.info('Generating source files');
  const apps = await fsPromises.readdir(`${repoRoot}/api/schemas/apps/`);
  await generateFiles(repoRoot, 'core');
  for (const app of apps) {
    await generateFiles(repoRoot, `apps/${app}`);
  }
}

main();
