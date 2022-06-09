package jolie.lang.parse.module;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import jolie.lang.parse.ParserException;
import jolie.lang.parse.Scanner;
import jolie.lang.parse.ast.Program;
import jolie.lang.parse.context.ParsingContext;

public class Modules {
	public static class ModuleParsedResult {

		/**
		 * An ast program of root program
		 */
		private final Program mainProgram;

		/**
		 * Collection of symbol table reference for main program execution
		 */
		private final Map< URI, SymbolTable > symbolTables;

		private final Map< URI, Map< SymbolInfo, List< ParsingContext > > > allSymbolReferences;

		private ModuleParsedResult( Program mainProgram, Map< URI, SymbolTable > symbolTables,
			Map< URI, Map< SymbolInfo, List< ParsingContext > > > allSymbolReferences ) {
			this.mainProgram = mainProgram;
			this.symbolTables = symbolTables;
			this.allSymbolReferences = allSymbolReferences;
		}

		public Program mainProgram() {
			return mainProgram;
		}

		public Map< URI, SymbolTable > symbolTables() {
			return symbolTables;
		}

		public Map< URI, Map< SymbolInfo, List< ParsingContext > > > allSymbolReferences() {
			return allSymbolReferences;
		}
	}

	public static ModuleParsedResult parseModule( ModuleParsingConfiguration configuration, InputStream stream,
		URI programDirectory )
		throws ParserException, IOException, ModuleException {
		ModuleParser parser = new ModuleParser( configuration );
		ModuleFinder finder = new ModuleFinderImpl( configuration.packagePaths() );

		ModuleRecord mainRecord = parser.parse(
			new Scanner( stream, programDirectory, configuration.charset(), configuration.includeDocumentation() ) );

		ModuleCrawler.CrawlerResult crawlResult = ModuleCrawler.crawl( mainRecord, configuration, finder );

		Map< URI, Map< SymbolInfo, List< ParsingContext > > > allSymbolReferences =
			SymbolReferenceResolver.resolve( crawlResult );

		ModuleParsedResult result =
			new ModuleParsedResult( mainRecord.program(), crawlResult.symbolTables(), allSymbolReferences );

		return result;
	}
}
