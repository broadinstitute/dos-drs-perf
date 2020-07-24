# dos-drs-perf

## Description

Runs gatling against DOS & DRS servers directly and via Martha.

## Running Tests

### Run all tests

```bash
sbt gatling:test
```

### Running one test

```bash
sbt "gatling:testOnly dos_drs_perf.JdrSimulation"
```
## Application Default Credentials and Jade Data Repo

Jade Data Repo simulations currently use the gcloud Application Default Credentials.

Login using `gcloud auth application-default login`.

## Rendering the default firecloud_account.json

If you have authenticated to vault in the last 30 days you can render the file using:
```bash
docker run \
  --rm \
  --volume ${HOME}:/root \
  --volume "${PWD}:${PWD}" \
  --env "INPUT_PATH=${PWD}/src/test/resources" \
  --env "OUT_PATH=${PWD}/target/scala-2.12/test-classes" \
  --env "ENVIRONMENT=dev" \
  broadinstitute/dsde-toolbox render-templates.sh
```

If you do not have permission to rend er the configuration, you can also override the optional config.

## Linking to Bond for dos_drs_perf.MarthaMockDrsSimulation

The `dos_drs_perf.MarthaMockDrsSimulation` requires your `gcloud auth application-default login` service account to be
linked in Bond.

By default, visit https://bvdp-saturn-dev.appspot.com/#profile with your application-default account and link to NCI.

After https://broadworkbench.atlassian.net/browse/WA-288 is completed one may remove this verification from the code.

## Optional Config / Environment Variables

See the [reference.conf](src/test/resources/reference.conf) for a list of configuration variables.

## Clean / Reset

To erase all gatling results, rendered service accounts, etc. run `sbt clean`.
