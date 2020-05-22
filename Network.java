

public class Network {
    private double[][] output;
    private double[][][] weights;
    private double[][] bias;
    private double[][] error_signal;
    private double[][] output_derivative;
    public final int[] NETWORK_LAYER_SIZES;
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    public final int NETWORK_SIZE;

    public Network(int... NETWORK_LAYER_SIZES) {
        this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
        this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
        this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
        this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[this.NETWORK_SIZE - 1];
        this.output = new double[this.NETWORK_SIZE][];
        this.weights = new double[this.NETWORK_SIZE][][];
        this.bias = new double[this.NETWORK_SIZE][];
        this.error_signal = new double[this.NETWORK_SIZE][];
        this.output_derivative = new double[this.NETWORK_SIZE][];

        for(int i = 0; i < this.NETWORK_SIZE; ++i) {
            this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
            this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], -0.5D, 0.7D);
            if (i > 0) {
                this.weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], NETWORK_LAYER_SIZES[i - 1], -1.0D, 1.0D);
            }
        }

    }

    public double[] calculate(double... input) {
        if (input.length != this.INPUT_SIZE) {
            return null;
        } else {
            this.output[0] = input;

            for(int layer = 1; layer < this.NETWORK_SIZE; ++layer) {
                for(int neuron = 0; neuron < this.NETWORK_LAYER_SIZES[layer]; ++neuron) {
                    double sum = this.bias[layer][neuron];

                    for(int prevNeuron = 0; prevNeuron < this.NETWORK_LAYER_SIZES[layer - 1]; ++prevNeuron) {
                        sum += this.output[layer - 1][prevNeuron] * this.weights[layer][neuron][prevNeuron];
                    }

                    this.output[layer][neuron] = this.sigmoid(sum);
                    this.output_derivative[layer][neuron] = this.output[layer][neuron] * (1.0D - this.output[layer][neuron]);
                }
            }

            return this.output[this.NETWORK_SIZE - 1];
        }
    }

    public void train(TrainSet set, int loops, int batch_size) {
        if (set.INPUT_SIZE == this.INPUT_SIZE && set.OUTPUT_SIZE == this.OUTPUT_SIZE) {
            for(int i = 0; i < loops; ++i) {
                TrainSet batch = set.extractBatch(batch_size);

                for(int b = 0; b < batch_size; ++b) {
                    this.train(batch.getInput(b), batch.getOutput(b), 0.3D);
                }

            }

        }
    }


    public void train(double[] input, double[] target, double eta) {
        if (input.length == this.INPUT_SIZE && target.length == this.OUTPUT_SIZE) {
            this.calculate(input);
            this.backpropError(target);
            this.updateWeights(eta);
        }
    }

    public void backpropError(double[] target) {
        int layer;
        for(layer = 0; layer < this.NETWORK_LAYER_SIZES[this.NETWORK_SIZE - 1]; ++layer) {
            this.error_signal[this.NETWORK_SIZE - 1][layer] = (this.output[this.NETWORK_SIZE - 1][layer] - target[layer]) * this.output_derivative[this.NETWORK_SIZE - 1][layer];
        }

        for(layer = this.NETWORK_SIZE - 2; layer > 0; --layer) {
            for(int neuron = 0; neuron < this.NETWORK_LAYER_SIZES[layer]; ++neuron) {
                double sum = 0.0D;

                for(int nextNeuron = 0; nextNeuron < this.NETWORK_LAYER_SIZES[layer + 1]; ++nextNeuron) {
                    sum += this.weights[layer + 1][nextNeuron][neuron] * this.error_signal[layer + 1][nextNeuron];
                }

                this.error_signal[layer][neuron] = sum * this.output_derivative[layer][neuron];
            }
        }

    }

    public void updateWeights(double eta) {
        for(int layer = 1; layer < this.NETWORK_SIZE; ++layer) {
            for(int neuron = 0; neuron < this.NETWORK_LAYER_SIZES[layer]; ++neuron) {
                double delta = -eta * this.error_signal[layer][neuron];
                double[] var10000 = this.bias[layer];
                var10000[neuron] += delta;

                for(int prevNeuron = 0; prevNeuron < this.NETWORK_LAYER_SIZES[layer - 1]; ++prevNeuron) {
                    var10000 = this.weights[layer][neuron];
                    var10000[prevNeuron] += delta * this.output[layer - 1][prevNeuron];
                }
            }
        }

    }

    private double sigmoid(double x) {
        return 1.0D / (1.0D + Math.exp(-x));
    }

}

