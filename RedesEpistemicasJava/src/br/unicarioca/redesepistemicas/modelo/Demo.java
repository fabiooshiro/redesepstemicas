/**
 * Copyright (c) 2009, lachlan.gregor@gmail.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met: Redistributions of source code 
 * must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of 
 * conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution. Neither the name of the RMIT University Ð Melbourne, Australia 
 * nor the names of its contributors may be used to endorse or promote products derived from this 
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER 
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS 
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;

import com.rmit.neuralnetwork.NeuralNetwork;
import com.rmit.neuralnetwork.training.BackPropagation;
import com.rmit.neuralnetwork.training.Training;
import com.rmit.neuralnetwork.trainingdata.TrainingExample;
import com.rmit.neuralnetwork.trainingdata.TrainingSet;

/**
 * @author lachlan
 *
 */
public class Demo {

	private NeuralNetwork neuralNetwork;
	
	public Demo() {
		
		// define the structure of the neural network
		int [] neuralNetworkStructure = new int[]{3, 1, 2};
		
		// create new neural network with the defined structure
		neuralNetwork = new NeuralNetwork(neuralNetworkStructure);
		
		// training settings
		//int numberEvaluations = 3000; 
		int numberEvaluations = 1000;
		double errorTolerance = 0.0; 
		
		// pso specific settings
		int numberParticles = 20; 
		double learningFactor = 1.49618; 
		double inertialWeight = 0.7298; 
		
		// create a instance of a training method
		//Training training = new ParticleSwarmOptimization(numberEvaluations, errorTolerance, learningFactor, inertialWeight, numberParticles);
		Training training = new BackPropagation(numberEvaluations,errorTolerance);
		
		// get the training set
		//DataLoader trainingData = new DataLoader(DataLoader.CLASSIFICATION, "../neuralnetworkdata/xor.csv", 2, 1, 100, 0, 1);
		
		ArrayList<TrainingExample> listTraining = new ArrayList<TrainingExample>();
		{
			TrainingExample te = new TrainingExample();
			ArrayList<Double> in = new ArrayList<Double>();
			in.add(1.0);
			in.add(1.0);
			in.add(1.0);
			ArrayList<Double> out = new ArrayList<Double>();
			out.add(0.7);
			out.add(0.7);
			te.setInputs(in);
			te.setOutputs(out);
			listTraining.add(te);
		}
		{
			TrainingExample te = new TrainingExample();
			ArrayList<Double> in = new ArrayList<Double>();
			in.add(0.5);
			in.add(1.0);
			in.add(1.0);
			ArrayList<Double> out = new ArrayList<Double>();
			out.add(0.3);
			out.add(0.3);
			te.setInputs(in);
			te.setOutputs(out);
			listTraining.add(te);
		}
		{
			TrainingExample te = new TrainingExample();
			ArrayList<Double> in = new ArrayList<Double>();
			in.add(0.5);
			in.add(0.5);
			in.add(1.0);
			ArrayList<Double> out = new ArrayList<Double>();
			out.add(0.9);
			out.add(0.9);
			te.setInputs(in);
			te.setOutputs(out);
			listTraining.add(te);
		}
		TrainingSet trainingSet = new TrainingSet("MyT",listTraining);
		
		
		// set the training method and set for the neural network to use
		neuralNetwork.setTraining(training);
		//neuralNetwork.setTrainingSet(trainingData.getTrainingSet());
		neuralNetwork.setTrainingSet(trainingSet);
		
	}

	private void train() {	
		neuralNetwork.train();
	}

	private void predict(Double a,Double b) {
		ArrayList<Double> inputs = new ArrayList<Double>();
		ArrayList<Double> outputs = new ArrayList<Double>();
		
		inputs.add(a);
		inputs.add(b);
		inputs.add(1.0);
		System.out.println("inputs "+a+" "+b);
		
		neuralNetwork.setInputs(inputs);
		outputs = neuralNetwork.getOutputs();
		
		for (Double output : outputs) {
			System.out.println("predicted output " + output);
		}
	}
	
	public static void main(String[] args) {
		
		System.out.println("starting ...");
		Demo main = new Demo();
		
		System.out.println("training neural network ...");
		main.train();

		System.out.println("predicting neural network ...");
		main.predict(1.0,1.0);
		System.out.println("predicting neural network ...");
		main.predict(0.5,1.0);
		System.out.println("predicting neural network ...");
		main.predict(0.5,0.5);
		
		System.out.println("finished successfully !");
	}
}
