# Apache Camel LSP Eclipse client UI Tests

## Structure

 - com.github.camel-tooling.lsp.eclipse.client.tests.ui/plugins/com.github.cameltooling.lsp.reddeer
   RedDeer Test framework
 - com.github.camel-tooling.lsp.eclipse.client.tests.ui/tests/com.github.cameltooling.lsp.ui.tests
   UI tests based on the above RedDeer framework for Camel Tooling

## Executing tests from command line

The UI smoke tests are enabled by default. You can also execute other test suite by specifying

    -DtestUIClass=CamelLSPCompletionTest

To disable UI tests you need to set

    -DskipUITests=true

It is also recommended to ignore local artifacts

    -Dtycho.localArtifacts=ignore

You may also get errors from baseline comparison, so disable it

    -Dtycho.baseline=disable
    -DskipBaselineComparison=true

You also need to specify target platform

    camel-lsp-target-platform

Executing only UI tests should look like as follows

    mvn clean verify -pl \
        camel-lsp-target-platform, \
        com.github.camel-tooling.lsp.eclipse.client.tests.ui/plugins/com.github.cameltooling.lsp.reddeer, \
        com.github.camel-tooling.lsp.eclipse.client.tests.ui/tests/com.github.cameltooling.lsp.ui.tests \
        -am \
        -DskipUITests=false \
        -Dtycho.localArtifacts=ignore \
        -Dtycho.baseline=disable \
        -DskipBaselineComparison=true \
        -DfailIfNoTests=false \
        -Dtest=CamelLSPCompletionTest

## Debugging tests when running from command line

Just add the following system property

    -DdebugPort=8001

and in IDE create a configuration for Remote Java Application in Run > Debug Configurations...

### Executing tests from IDE

1. Install Camel Tooling LSP Eclipse client, e.g from https://camel-tooling.github.io/camel-lsp-client-eclipse-update-site/updatesite/nightly/
2. Install RedDeer 3.x, e.g. from http://download.eclipse.org/reddeer/releases/3.1.0/
3. Import the tests as Existing Projects into Workspace
4. Now, we can launch 'RedDeer Test' which is available in Run configuration
