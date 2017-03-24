package com.citi.hackathon;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreNLPProtos.CorefChain;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class BasicPipelineExample {
	
	public static void main(String[] args){
		
		Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse ,mention, coref, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        // read some text in the text variable
        String text = " Barack Obama was born in Hawaii.  He is the president. Obama was elected in 2008.";
        
     // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        
     // these are all the sentences in this document
     // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
     List<CoreMap> sentences = document.get(SentencesAnnotation.class);

     for(CoreMap sentence: sentences) {
       // traversing the words in the current sentence
       // a CoreLabel is a CoreMap with additional token-specific methods
       for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
         // this is the text of the token
         String word = token.get(TextAnnotation.class);
         
         System.out.println(" Word "+word);
         // this is the POS tag of the token
         String pos = token.get(PartOfSpeechAnnotation.class);
         System.out.println("Parst of Speech "+pos);
         // this is the NER label of the token
         String ne = token.get(NamedEntityTagAnnotation.class);
         System.out.println(" Named Entities "+ne);
         
         String lemma = token.get(LemmaAnnotation.class);
         System.out.println(" Lemma "+lemma);
         
         Tree sentimentTree = token.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
         System.out.println("Sentiment "+sentimentTree);
       }

       // this is the parse tree of the current sentence
       Tree tree = sentence.get(TreeAnnotation.class);
       
     

       // this is the Stanford dependency graph of the current sentence
       SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
     }

     // This is the coreference link graph
     // Each chain stores a set of mentions that link to each other,
     // along with a method for getting the most representative mention
     // Both sentence and token offsets start at 1!
 
     
     for (edu.stanford.nlp.coref.data.CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
         System.out.println("\t" + cc);
       }
       for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
         System.out.println("---");
         System.out.println("mentions");
         for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
           System.out.println("\t" + m);
          }
       }

		
	}

}
