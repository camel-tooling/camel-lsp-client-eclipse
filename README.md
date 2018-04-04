[![GitHub tag](https://img.shields.io/github/tag/camel-tooling/camel-lsp-client-eclipse.svg?style=plastic)]()
[![Build Status](https://travis-ci.org/camel-tooling/camel-lsp-client-eclipse.svg?branch=master)](https://travis-ci.org/camel-tooling/camel-lsp-client-eclipse)
[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)]()
[![Gitter](https://img.shields.io/gitter/room/camel-tooling/Lobby.js.svg)](https://gitter.im/camel-tooling/Lobby)


How to debug Camel Language Server from Eclipse client
======================================================

1. Add debug arguments to the list com.github.camel-tooling.eclipse.client.CamelLSPStreamConnectionProvider.computeCommands()

You will end up with something like:

	private static List<String> computeCommands() {
		List<String> commands = new ArrayList<>();
		commands.add("java");
		commands.addAll(debugArguments());
		commands.add("-jar");
		commands.add(computeCamelLanguageServerJarPath());
		return commands;
	}

	private static List<String> debugArguments() {
		return Arrays.asList("-Xdebug","-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=3000");
	}
	
	
2. Create a Remote Java Application Debug Launch configuration in Eclipse

