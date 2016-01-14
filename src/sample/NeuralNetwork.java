package sample;

import java.util.Random;

/**
 * Created by tombe on 23/11/2015.
 */
public class NeuralNetwork {

    private Random random;

    private int _numberOfInputCells;
    private int _numberOfHiddenCells;
    private int _numberOfOutputCells;

    private double[] inputNeuron;
    private double[] hiddenNeuron;
    private double[] outputNeuron;
    private double[] hiddenBias;
    private double[] outputBias;

    private double[][] ihAxon;
    private double[][] hoAxon;

    private double[] targets;

    private double[] newHiddens;

    private double[] outputGradients;
    private double[] hiddenGradients;

    private double[][] ihPreviousWeightsDelta;
    private double[][] hoPreviousWeightsDelta;
    private double[] hPreviousBiasesDelta;
    private double[] oPreviousBiasesDelta;

    private double learningRate = 0.5;
    private double momentum = 0.1;

    public NeuralNetwork(int amount) {
        _numberOfInputCells = amount;
        _numberOfHiddenCells = 4;
        _numberOfOutputCells = 2;

        newHiddens = new double[_numberOfHiddenCells];
        ihPreviousWeightsDelta = new double[_numberOfInputCells][_numberOfHiddenCells];
        hoPreviousWeightsDelta = new double[_numberOfHiddenCells][_numberOfOutputCells];
        hPreviousBiasesDelta = new double[_numberOfHiddenCells];
        oPreviousBiasesDelta = new double[_numberOfOutputCells];

        //initializeInputNeuron();
        //initializeHiddenNeuron();
        //initializeOutputNeuron();
        //initializeWeights();
        //initializeTargets();
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void initializeInputNeuron(int amount, String[] list){
        inputNeuron = new double[amount];
        for (int i = 0 ; i <_numberOfInputCells; i++){
            inputNeuron[i] = Double.parseDouble(list[i]);
        }
    }

    public void initializeHiddenNeuron(){
        hiddenNeuron = new double[_numberOfHiddenCells];
        for (int i = 0; i < hiddenNeuron.length; i++){
            hiddenNeuron[i]=0.0;
        }
    }

    public void initializeOutputNeuron(){
        outputNeuron = new double[_numberOfOutputCells];
        outputNeuron[0] = 0.0;
        outputNeuron[1] = 0.0;
    }

    public void initializeWeights() {

        double initializeCounter = 0.001;


        ihAxon = new double[inputNeuron.length][hiddenNeuron.length];
        for (int i = 0; i < inputNeuron.length; i++) {
            for (int j = 0; j < hiddenNeuron.length; j++) {
                ihAxon[i][j] = initializeCounter;
                initializeCounter+=0.001;
            }
        }

        hiddenBias = new double[_numberOfHiddenCells];

        for (int i =0; i <hiddenBias.length; i++) {
            hiddenBias[i]= initializeCounter;
            initializeCounter+=0.001;
        }

        hoAxon = new double[_numberOfHiddenCells][_numberOfOutputCells];
        for (int i = 0; i < hiddenNeuron.length; i++){
            for (int j = 0; j<outputNeuron.length; j++){
                hoAxon[i][j] = initializeCounter;
                initializeCounter++;
            }
        }

        outputBias = new double[_numberOfOutputCells];

        for (int i =0; i <outputBias.length; i++) {
            outputBias[i]= initializeCounter;
            initializeCounter+=0.001;
        }
    }

    public void initializeTargets(){
        targets = new double[_numberOfOutputCells];
        random = new Random();
        targets[0] = random.nextDouble();
        targets[1] = random.nextDouble();
    }

    public int get_numberOfInputCells() {
        return _numberOfInputCells;
    }

    public int get_numberOfHiddenCells() {
        return _numberOfHiddenCells;
    }

    public int get_numberOfOutputCells() {
        return _numberOfOutputCells;
    }

    public double[] getInputNeuron() {
        return inputNeuron;
    }

    public double[] getHiddenNeuron() {
        return hiddenNeuron;
    }

    public double[] getOutputNeuron() {
        return outputNeuron;
    }

    public double[] getHiddenBias() {
        return hiddenBias;
    }

    public double[] getOutputBias() {
        return outputBias;
    }

    public double[][] getIhAxon() {
        return ihAxon;
    }

    public double[][] getHoAxon() {
        return hoAxon;
    }

    public double[] getTargets() {
        return targets;
    }

    public double[] getNewHiddens() {
        return newHiddens;
    }

    public double[] computeOutputs() {
        //double[] outputs = new double[_numberOfOutputCells];

        for (int j = 0; j < hiddenNeuron.length; j++) {
            double total = 0;
            for (int i = 0; i < inputNeuron.length; i++) {
                total += inputNeuron[i] * ihAxon[i][j];
            }
            total += hiddenBias[j];
            hiddenNeuron[j] = total;
        }
        for (int i =0; i < hiddenNeuron.length;i++) {

            newHiddens[i] = HyperTanFunction(hiddenNeuron[i]);
        }

        for (int i = 0; i < outputNeuron.length; i++) {
            double total = 0;
            for (int j = 0; j < hiddenNeuron.length; j++){
                total += newHiddens[j]* hoAxon[j][i];
            }
            total+= outputBias[i];
            outputNeuron[i] = total;

            outputNeuron[i] = SigmoidFunction(outputNeuron[i]);
        }



        return outputNeuron;
    }

    public static double HyperTanFunction(double x)
    {
        if (x < -45.0)
            return -1.0;
        if (x > 45.0)
            return 1.0;
        return Math.tanh(x);
    }

    public static double SigmoidFunction(double x)
    {
        if (x < -45.0)
            return 0.0;
        if (x > 45.0)
            return 1.0;
        return 1.0/(1.0 + Math.exp(-x));
    }

    public double calculateError(){
        double error = 0;
        for (int i = 0; i < outputNeuron.length; i++) {
            error  += Math.pow(targets[i] - outputNeuron[i], 2);

        }

        return Math.sqrt(error);
    }

    public void updateWeights(){
        outputGradients = new double[_numberOfOutputCells];
        double derivative = 0;
        for (int i = 0; i < outputNeuron.length; i++){

            derivative = (1 - outputNeuron[i]) * outputNeuron[i];
            outputGradients[i] = derivative * (targets[i] - outputNeuron[i]);
        }
        hiddenGradients = new double[_numberOfHiddenCells];

        double sum = 0;

        for (int i = 0; i < newHiddens.length; i++){
            derivative = (1-newHiddens[i]) * (1+newHiddens[i]);


            for (int j = 0; j < outputGradients.length; j++){
                sum += outputGradients[j] * hoAxon[i][j];
            }
            hiddenGradients[i] = derivative * sum;

        }

        //Calculate delta difference between input and hidden weights
        double delta =0;
        for (int i = 0; i < _numberOfInputCells; i++){
            for (int j =0; j < _numberOfHiddenCells; j++){
                delta = learningRate * hiddenGradients[j] * inputNeuron[i];
                ihAxon[i][j] += delta;
                ihAxon[i][j] += momentum * ihPreviousWeightsDelta[i][j];

                ihPreviousWeightsDelta[i][j] = delta;
            }
        }

        for (int i = 0; i < _numberOfHiddenCells; i ++){
            for (int j =0; j < _numberOfOutputCells; j++){
                delta = learningRate * outputGradients[j] * hiddenNeuron[i];
                //System.out.println("hidden output delta: " + delta);
                hoAxon[i][j] += delta;
                hoAxon[i][j] += momentum * hoPreviousWeightsDelta[i][j];
                hoPreviousWeightsDelta[i][j] = delta;
            }
        }

        for (int i = 0; i < _numberOfHiddenCells; i++){
            delta = learningRate * hiddenGradients[i];
            hiddenBias[i] += delta;
            hiddenBias[i]  += momentum * hPreviousBiasesDelta[i];

            hPreviousBiasesDelta[i] = delta;
        }

        for (int i = 0; i < _numberOfOutputCells; i++){
            delta = learningRate * outputGradients[i];
            outputBias[i] += delta;
            outputBias[i] += momentum * oPreviousBiasesDelta[i];

            oPreviousBiasesDelta[i] = delta;
        }

        /*this.initializeInputNeuron();
        this.initializeHiddenNeuron();
        this.initializeOutputNeuron();*/

    }




}
