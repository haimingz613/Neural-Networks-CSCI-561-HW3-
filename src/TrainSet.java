import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class TrainSet {
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    private ArrayList<double[][]> data = new ArrayList();

    public TrainSet(int INPUT_SIZE, int OUTPUT_SIZE) {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public void addData(double[] in, double[] expected) {
        if (in.length == this.INPUT_SIZE && expected.length == this.OUTPUT_SIZE) {
            this.data.add(new double[][]{in, expected});
        }
    }

    public TrainSet extractBatch(int size) {
        if (size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(this.INPUT_SIZE, this.OUTPUT_SIZE);
            Integer[] ids = NetworkTools.randomValues(0, this.size() - 1, size);
            Integer[] var4 = ids;
            int var5 = ids.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Integer i = var4[var6];
                set.addData(this.getInput(i), this.getOutput(i));
            }

            return set;
        } else {
            return this;
        }
    }


    public int size() {
        return this.data.size();
    }

    public double[] getInput(int index) {
        return index >= 0 && index < this.size() ? ((double[][])this.data.get(index))[0] : null;
    }

    public double[] getOutput(int index) {
        return index >= 0 && index < this.size() ? ((double[][])this.data.get(index))[1] : null;
    }

    public int getINPUT_SIZE() {
        return this.INPUT_SIZE;
    }

    public int getOUTPUT_SIZE() {
        return this.OUTPUT_SIZE;
    }
}