import java.io.*;

public class Mnist {
    public static void main(String[] args) {
        Network network = new Network(784, 70, 35, 10);
        TrainSet set = createTrainSet();
        trainData(network, set, 10, 50, 100);

        TrainSet testSet = createTestSet();
        RunTrainSet(network, testSet);
    }

    public static TrainSet createTrainSet() {

        TrainSet set = new TrainSet(28 * 28, 10);
        File csv1 = new File("train_image.csv");
        File csv2 = new File("train_label.csv");
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        try {
            br1 = new BufferedReader(new FileReader(csv1));
            br2 = new BufferedReader(new FileReader(csv2));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line1 = "";
        String line2 = "";
        String everyLine1 = "";
        String everyLine2 = "";

        try {


            while ((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null)
            {
                everyLine1 = line1;
                everyLine2 = line2;
                double[] input = new double[28 * 28];
                double[] output = new double[10];
                output[Integer.parseInt(everyLine2)] = 1d;
                String[] s = everyLine1.split(",");     // the only this I need to consider
                for(int i = 0; i < s.length; i++){
                    input[i] = (double)Integer.parseInt(s[i]) / (double)255;
                }
                set.addData(input, output);
            }
            br1.close();
            br2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static TrainSet createTestSet() {

        TrainSet set = new TrainSet(28 * 28, 10);
        File csv = new File("test_image.csv");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        String everyLine = "";

        try {


            while ((line = br.readLine()) != null)
            {
                everyLine = line;
                double[] input = new double[28 * 28];
                double[] output = new double[10];
                String[] s = everyLine.split(",");
                for(int i = 0; i < s.length; i++){
                    input[i] = (double)Integer.parseInt(s[i]) / (double)255;
                }
                set.addData(input, output);
            }
            br.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void trainData(Network net,TrainSet set, int epochs, int loops, int batch_size) {
        for(int e = 0; e < epochs; e++) {
            net.train(set, loops, batch_size);
        }
    }

    public static void RunTrainSet(Network net, TrainSet set) {
        try {
            File csv3 = new File("test_predictions.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(csv3, true));
            for(int i = 0; i < set.size(); i++) {
                int highest = NetworkTools.indexOfHighestValue(net.calculate(set.getInput(i)));
                bw.write(highest+"\n");
            }
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

