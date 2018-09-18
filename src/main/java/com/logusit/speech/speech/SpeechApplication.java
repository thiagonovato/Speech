package com.logusit.speech.speech;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

@SpringBootApplication
public class SpeechApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SpeechApplication.class, args);

		try (SpeechClient speechClient = SpeechClient.create()) {

			System.out.println("Entrou no laço...");
			
			String fileName = "./resources/audio.wav";
			Path path = Paths.get(fileName);
			byte[] data = Files.readAllBytes(path);
			ByteString audioBytes = ByteString.copyFrom(data);
			
			System.out.println("Pegou o arquivo...");

			RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(AudioEncoding.LINEAR16)
					//.setSampleRateHertz(16000)
					.setLanguageCode("pt-BR").build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

			System.out.println("Primeiro reco...");
			
			RecognizeResponse response = speechClient.recognize(config, audio);
			List<SpeechRecognitionResult> results = response.getResultsList();
			
			System.out.println("Segundo reco...");

			for (SpeechRecognitionResult result : results) {
				// There can be several alternative transcripts for a given chunk of speech.
				// Just use the
				// first (most likely) one here.
				
				System.out.println("Aguardando resultado...");
				
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				System.out.printf("Transcription: %s%n", alternative.getTranscript());
			}

		}
	}
}
