package br.unicarioca.redesepistemicas.modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import com.rmit.neuralnetwork.NeuralNetwork;
import com.rmit.neuralnetwork.activation.Activation;
import com.rmit.neuralnetwork.node.Connection;
import com.rmit.neuralnetwork.node.Node;
import com.rmit.neuralnetwork.node.Weight;
import com.rmit.neuralnetwork.training.Training;
import com.rmit.neuralnetwork.trainingdata.TrainingExample;
import com.rmit.neuralnetwork.trainingdata.TrainingSet;

/**
 * @author lachlan
 *
 */
public class BackPropagation extends Training {
	private static Logger logger = Logger.getLogger(BackPropagation.class);
	private double learningRate;
	private double momentum;
	
	public BackPropagation(int numberEvaluations, double errorTolerance) {
		
		super(numberEvaluations, errorTolerance);

		this.learningRate = 0.9;
		this.momentum = 0.001;
		
		init();
	}
	
	public BackPropagation(int numberEvaluations, double errorTolerance, double learningRate, double momentum) {
		
		super(numberEvaluations, errorTolerance);

		this.learningRate = learningRate;
		this.momentum = momentum;
		
		init();
	}
	
	private void init() {
		setDetails("BACKPROP learningRate " + learningRate + " momentum " + momentum);
	}

	@Override
	public ArrayList<Double> train(NeuralNetwork neuralNetwork, TrainingSet trainingSet) {
		
		ArrayList<Double> errors = new ArrayList<Double>();
		ArrayList<TrainingExample> trainingExamples = trainingSet.getTrainingExamples();
		double error = Double.MAX_VALUE;
		double errorBest = Double.MAX_VALUE;

		int numberTrainingExamples = trainingExamples.size();
		
		// reset the weights
		//neuralNetwork.initWeights();
		
		for (int current = 0; current < numberEvaluations && error > errorTolerance; current++) {
			
			error = 0;
			
			// go through each training example
			for (TrainingExample trainingExample : trainingExamples) {

				// pass in the new inputs to the nn
				neuralNetwork.setInputs(trainingExample.getInputs());
				
				neuralNetwork.getOutputs();
				
				// ******************* USE THIS FOR ONLINE ERROR ************************
				// get the predicted outputs + calculate outputs error
				// error += getError(trainingExample.getOutputs(), neuralNetwork.getOutputs());
				
				// update weights by back propagating errors
				backPropagate(neuralNetwork, trainingExample.getOutputs());
			}
			
			// ********************* THIS IS FOR OFFLINE ERROR ****************************
			// calculate offline error (real error)
			for (TrainingExample trainingExample : trainingExamples) {

				// pass in the new inputs to the nn
				neuralNetwork.setInputs(trainingExample.getInputs());
				
				// get the predicted outputs + calculate outputs error
				error += getError(trainingExample.getOutputs(), neuralNetwork.getOutputs());
			}
			// ****************************************************************************
			
			// get the mean of the summed squared error of one epoch
			error = error/numberTrainingExamples;
			
			// get best error
			if(error < errorBest) {
				errorBest = error;
			}
			
			// save the errors for output
			errors.add(errorBest);		
		}		
		
		logger.debug("BACKPROP " + errorBest);
		return errors;
	}

	private void backPropagate(NeuralNetwork neuralNetwork, ArrayList<Double> trainingSetOutputs) {
		
		double value = 0.0;
		double delta = 0.0;
		double error = 0.0;
		double errorGradient = 0.0;
		
		Iterator<Double> trainingSetOutputsIterator = trainingSetOutputs.iterator();
		Activation activation = neuralNetwork.getActivation();
		
		// iterator backwards through the neural networks graph
		for (ListIterator<Node> graphIterator = neuralNetwork.listIteratorEnd(); graphIterator.hasPrevious();) {
			
			Node node = graphIterator.previous();		
			value = node.getValue();
			
			if (node.getType() == Node.OUTPUT) { 
				
				error = getErrorOutput(trainingSetOutputsIterator.next(), value);		
				
			} else if (node.getType() == Node.HIDDEN) {
				
				error = getErrorHidden(value, node.getOutputConnections());
			}

			// error gradient
			errorGradient = error * activation.getGradient(value);
			
			// update the gradient so the children can use next
			node.setErrorGradient(errorGradient);
			
			for (Connection inputConnection : node.getInputConnections()) {
				
				Weight weight = inputConnection.getWeight();
				
				delta = learningRate * inputConnection.getNode().getValue() * errorGradient + momentum * weight.getDelta();
								
				// update the weight and save the delta
				weight.updateWeight(delta);
			}
		}
	}
	
	private double getErrorOutput(double outputTrainingSet, double outputNeuralNetwork){

		return (outputTrainingSet - outputNeuralNetwork);
	}

	private double getErrorHidden(double nodeValue, ArrayList<Connection> outputConnections){
		
		double sum = 0.0;

		for (Connection outputConnection : outputConnections) {
			
			sum += outputConnection.getWeight().getWeight() * outputConnection.getNode().getErrorGradient();
		}
		return sum;
	}
}
