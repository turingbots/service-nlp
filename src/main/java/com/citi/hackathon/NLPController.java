package com.citi.hackathon;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.citi.hackathon.NLPOutput.WordMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class NLPController {

	private static Logger log = LoggerFactory.getLogger(NLPController.class);

	@Autowired
	private NLPService service;

	@ApiOperation(value = "Tokenize String input based on NLP algorithm ", nickname = "NLP parse", response = NLPResponse.class,notes = "Possible values for entity type DATE, TIME, DURATION, MONEY, PERCENT, or NUMBER")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK", response = NLPResponse.class),
			@ApiResponse(code = 404, message = "NOT_FOUND", response = ErrorResponse.class),
			@ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ErrorResponse.class) })
	@RequestMapping(value = "/intents", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<?> parseIntents(@RequestBody NLPInput request) {

		String input = request.getInput();
		log.debug("Input text " + input);

		List<NLPOutput> parsedOutput = service.parse(input);
		List<Intent> intents = new ArrayList<>();
		String intentText = null;
		String namedEntity = null;
		String lemmaText=null;

		for (NLPOutput nlpOutput : parsedOutput) {

			intentText = null;
			namedEntity=null;
			lemmaText=null;
			String word = nlpOutput.getWord();
			WordMapper pos = nlpOutput.getPartsOfSpeech();
			WordMapper lemma = nlpOutput.getLemma();
			WordMapper ne = nlpOutput.getNamedEntity();

			if (pos != null && (pos.getMappedElement().equals("NN") || pos.getMappedElement().equals("NNS") || pos.getMappedElement().equals("NNPS"))) {
				intentText = word;

			} else if (ne != null && ne.getMappedElement() != null && !ne.getMappedElement().equals("O")) {
				intentText = ne.getWord();
				namedEntity = ne.getMappedElement();
			}
			else if (word != null && !word.equalsIgnoreCase(lemma.getMappedElement())) {
				lemmaText = lemma.getMappedElement();
			}

			if (intentText != null) {

				Intent intent = new Intent();
				intent.setIntentText(intentText);
				if (namedEntity != null) {
					intent.setEntityType(namedEntity);
				}
				intents.add(intent);
			}
			
		}
		if(intents.isEmpty() && lemmaText!=null && !lemmaText.isEmpty()){
			
			Intent intent = new Intent();
			intent.setIntentText(lemmaText);
			if (namedEntity != null) {
				intent.setEntityType(namedEntity);
			}
			intents.add(intent);
		}

		NLPResponse nlpReponse = new NLPResponse();
		nlpReponse.setData(intents);
		return ResponseEntity.ok(nlpReponse);

	}

}
