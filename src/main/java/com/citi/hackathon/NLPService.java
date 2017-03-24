package com.citi.hackathon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.citi.hackathon.NLPOutput.WordMapper;

import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

@Component
public class NLPService {

	private static Logger log = LoggerFactory.getLogger(NLPService.class);
	
	private StanfordCoreNLP pipeline;
	
	public NLPService(){
		
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		pipeline = new StanfordCoreNLP(props);
		
	}

	public List<NLPOutput> parse(String text) {

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		List<NLPOutput> nlpOutput = new ArrayList<>();

		for(CoreMap sentence: sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods

			
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				NLPOutput output = new NLPOutput(); 
				String word = token.get(TextAnnotation.class);
				output.setWord(word);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);

				NLPOutput.WordMapper posMappper = populateAttributes(word, pos,"PartsOfSpeech");
				output.setPartsOfSpeech(posMappper);

				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				NLPOutput.WordMapper neMappper = populateAttributes(word, ne,"NamedEntity");
				output.setNamedEntity(neMappper);

				String lemma = token.get(LemmaAnnotation.class);
				NLPOutput.WordMapper lemmaMapper = populateAttributes(word, lemma,"Lemma");
				output.setLemma(lemmaMapper);

				Tree sentimentTree = token.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
				output.setSentimentTree(sentimentTree);
				
				nlpOutput.add(output);
			}

		}

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, edu.stanford.nlp.coref.data.CorefChain> graph = document.get(CorefChainAnnotation.class);
		
		return nlpOutput;


	}

	private NLPOutput.WordMapper populateAttributes(String word, String mappedElement, String mapperType) {

		NLPOutput.WordMapper posMappper = new WordMapper();
		posMappper.setWord(word);
		posMappper.setMappedElement(mappedElement);
		posMappper.setMapperType(mapperType);
		return posMappper;
	}

}
