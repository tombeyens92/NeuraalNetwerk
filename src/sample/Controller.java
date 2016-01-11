package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Button btn_initialise;
    public Button btn_start;

    private FileReader fileReader;

    private NeuralNetwork neuralNetwork;
    private int maxEpoch = 10000;
    private double errorThreshold = 0.000001;


    public TextField IH11;
    public TextField IH12;
    public TextField IH13;
    public TextField IH14;
    public TextField IH21;
    public TextField IH22;
    public TextField IH23;
    public TextField IH24;
    public TextField IH31;
    public TextField IH32;
    public TextField IH33;
    public TextField IH34;
    public TextField Input_1;
    public TextField Input_2;
    public TextField Input_3;
    public TextField Hidden_1;
    public TextField Hidden_2;
    public TextField Hidden_3;
    public TextField Hidden_4;
    public TextField HO11;
    public TextField HO12;
    public TextField HO21;
    public TextField HO22;
    public TextField HO31;
    public TextField HO32;
    public TextField HO41;
    public TextField HO42;
    public TextField Output_1;
    public TextField Output_2;


    public TextField[][] IH;
    public TextField[][] HO;
    public TextField[] Hidden;
    public TextField[] Output;


    public Label Target_1;
    public Label Target_2;

    public TextField Learning_rate;

    public TextField error_threshold;

    public Thread th;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileReader = new FileReader();
        String[] list = fileReader.readFile(".\\Files\\A.txt");

        //Switch to list to work with letter A otherwise 3
        this.neuralNetwork = new NeuralNetwork(3);
        //this.neuralNetwork = new NeuralNetwork(list.length);

        IH = new TextField[neuralNetwork.get_numberOfInputCells()][neuralNetwork.get_numberOfHiddenCells()];
        HO = new TextField[neuralNetwork.get_numberOfHiddenCells()][neuralNetwork.get_numberOfOutputCells()];
        Hidden = new TextField[neuralNetwork.get_numberOfHiddenCells()];
        Output = new TextField[neuralNetwork.get_numberOfOutputCells()];

        IH[0][0] = IH11;
        IH[0][1] = IH12;
        IH[0][2] = IH13;
        IH[0][3] = IH14;
        IH[1][0] = IH21;
        IH[1][1] = IH22;
        IH[1][2] = IH23;
        IH[1][3] = IH24;
        IH[2][0] = IH31;
        IH[2][1] = IH32;
        IH[2][2] = IH33;
        IH[2][3] = IH34;

        HO[0][0] = HO11;
        HO[0][1] = HO12;
        HO[1][0] = HO21;
        HO[1][1] = HO22;
        HO[2][0] = HO31;
        HO[2][1] = HO32;
        HO[3][0] = HO41;
        HO[3][1] = HO42;

        Hidden[0] = Hidden_1;
        Hidden[1] = Hidden_2;
        Hidden[2] = Hidden_3;
        Hidden[3] = Hidden_4;

        Output[0] = Output_1;
        Output[1] = Output_2;
    }


    public void initialise(ActionEvent actionEvent) {

        fileReader = new FileReader();
        String[] list = fileReader.readFile(".\\Files\\A.txt");

        String[] normalList = new String[]{"1", "-2","3"};

        neuralNetwork.initializeInputNeuron(normalList.length, normalList);
        //neuralNetwork.initializeInputNeuron(list.length, list);



        neuralNetwork.initializeHiddenNeuron();
        neuralNetwork.initializeOutputNeuron();
        neuralNetwork.initializeWeights();
        neuralNetwork.initializeTargets();
        double[] inputs = neuralNetwork.getInputNeuron();
        Input_1.setText(String.valueOf(inputs[0]));
        Input_2.setText(String.valueOf(inputs[1]));
        Input_3.setText(String.valueOf(inputs[2]));

        double[] hiddens = neuralNetwork.getHiddenNeuron();

        Hidden_1.setText(String.valueOf(hiddens[0]));
        Hidden_2.setText(String.valueOf(hiddens[1]));
        Hidden_3.setText(String.valueOf(hiddens[2]));
        Hidden_4.setText(String.valueOf(hiddens[3]));

        double[] targets = neuralNetwork.getTargets();
        Target_1.setText(String.valueOf(targets[0]));
        Target_2.setText(String.valueOf(targets[1]));


        //Deactivate when working with letter A
        for (int i =0; i < inputs.length; i++){
            for (int j =0; j < Hidden.length; j++){
                IH[i][j].setText("IH"+(i+1)+(j+1));
            }
        }

        for (int i =0; i < Hidden.length; i++){
            for (int j =0; j < Output.length; j++){
                HO[i][j].setText("HO"+(i+1)+(j+1));
            }
        }

        Output_1.setText("");
        Output_2.setText("");

        if (!Learning_rate.getText().isEmpty()){
            neuralNetwork.setLearningRate(Double.parseDouble(Learning_rate.getText()));
        }

        if (!error_threshold.getText().isEmpty()){
            this.errorThreshold = Double.parseDouble(error_threshold.getText());
        }

        System.out.println(neuralNetwork.getLearningRate() + " " + errorThreshold);
    }


    public void startBackProp(ActionEvent actionEvent) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < maxEpoch; i++){
                    Platform.runLater(() -> {
                        neuralNetwork.computeOutputs();
                        neuralNetwork.updateWeights();
                        changeValues();


                        if (errorThreshold > neuralNetwork.calculateError()){
                            stopBackProp();
                            return;
                        }

                    });
                    Thread.sleep(10);
                }
                return null;
            }
        };

        th = new Thread(task);
        th.setDaemon(true);
        th.start();



    }

    private void changeValues() {
        double[][] ihNetwork = neuralNetwork.getIhAxon();

        //Deactivate when working with letter A
        for (int i = 0; i < neuralNetwork.get_numberOfInputCells(); i++) {
            for (int j = 0; j < neuralNetwork.get_numberOfHiddenCells(); j++) {
                IH[i][j].setText(String.valueOf(ihNetwork[i][j]));
            }
        }
        double[][] hoNetwork = neuralNetwork.getHoAxon();
        for (int i = 0; i < neuralNetwork.get_numberOfHiddenCells(); i++) {
            for (int j = 0; j < neuralNetwork.get_numberOfOutputCells(); j++) {
                HO[i][j].setText(String.valueOf(hoNetwork[i][j]));
            }
        }

        double[] hiddenNeuron = neuralNetwork.getHiddenNeuron();

        for (int i = 0; i < neuralNetwork.get_numberOfHiddenCells(); i++) {
            Hidden[i].setText(String.valueOf(hiddenNeuron[i]));
        }

        double[] outputNeuron = neuralNetwork.getOutputNeuron();

        for (int i = 0; i < neuralNetwork.get_numberOfOutputCells(); i++) {
            Output[i].setText(String.valueOf(outputNeuron[i]));
        }

        neuralNetwork.initializeHiddenNeuron();
    }

    private void stopBackProp(){
        System.out.println("Stopped!");
        th.interrupt();
    }


}
