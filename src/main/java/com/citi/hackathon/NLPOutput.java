package com.citi.hackathon;

import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import io.swagger.annotations.ApiModelProperty;

public class NLPOutput {
	
    @ApiModelProperty(required = true)
	private String word;
    @ApiModelProperty(required = true)
	private WordMapper partsOfSpeech;
    @ApiModelProperty(required = true)
	private WordMapper lemma;
    @ApiModelProperty(required = true)
	private WordMapper namedEntity;
    @ApiModelProperty(required = true)
    private Tree sentimentTree;

	public static class WordMapper {
		private String word;
		private String mapperType;
		private String mappedElement;
		
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public String getMapperType() {
			return mapperType;
		}
		public void setMapperType(String mapperType) {
			this.mapperType = mapperType;
		}
		public String getMappedElement() {
			return mappedElement;
		}
		public void setMappedElement(String mappedElement) {
			this.mappedElement = mappedElement;
		}
	}


	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}


	public WordMapper getPartsOfSpeech() {
		return partsOfSpeech;
	}


	public void setPartsOfSpeech(WordMapper partsOfSpeech) {
		this.partsOfSpeech = partsOfSpeech;
	}


	public WordMapper getLemma() {
		return lemma;
	}


	public void setLemma(WordMapper lemma) {
		this.lemma = lemma;
	}


	public WordMapper getNamedEntity() {
		return namedEntity;
	}


	public void setNamedEntity(WordMapper namedEntity) {
		this.namedEntity = namedEntity;
	}
	

	public Tree getSentimentTree() {
		return sentimentTree;
	}


	public void setSentimentTree(Tree sentimentTree) {
		this.sentimentTree = sentimentTree;
	}

}
