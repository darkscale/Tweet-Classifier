package SVM.ca.uwo.csd.ai.nlp.libsvm.ex;

import SVM.ca.uwo.csd.ai.nlp.libsvm.svm;
import SVM.ca.uwo.csd.ai.nlp.libsvm.svm_model;
import SVM.ca.uwo.csd.ai.nlp.libsvm.svm_node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Syeed Ibn Faiz
 */
public class SVMPredictor {

    public static double[] predict(List<Instance> instances, svm_model model) {
        return predict(instances, model, true);
    }

    public static double[] predict(List<Instance> instances, svm_model model, boolean displayResult) {
        Instance[] array = new Instance[instances.size()];
        array = instances.toArray(array);
        return predict(array, model, displayResult);
    }

    public static double predict(Instance instance, svm_model model, boolean displayResult) {
        return svm.svm_predict(model, new svm_node(instance.getData()));
    }
    
    public static double predictProbability(Instance instance, svm_model model, double[] probabilities) {
        return svm.svm_predict_probability(model, new svm_node(instance.getData()), probabilities);
    }    
    public static double[] predict(Instance[] instances, svm_model model, boolean displayResult) 
    {
        int total = 0;
        int correct = 0;

        int tp = 0;
        int fp = 0;
        int fn = 0;

        boolean binary = model.nr_class == 2;
        double[] predictions = new double[instances.length];
        int count = 0;

        for (Instance instance : instances) 
        {
            double target = instance.getLabel();
            double p = svm.svm_predict(model, new svm_node(instance.getData()));
            predictions[count++] = p;

            ++total;
            if (p == target)
            {
                correct++;
                if (target > 0)
                {
                    tp++;
                }
            } 
            else if (target > 0)
            {
                fn++;
            } 
            else 
            {
                fp++;
            }
        }
        //check
        if (displayResult) {
            /*System.out.print("Accuracy = " + (double) correct / total * 100
                    + "% (" + correct + "/" + total + ") (classification)\n");*/

            if (binary) {
            //	System.out.println(tp+" "+fp+" "+fn);
                double precision = (double) tp / (tp + fp);
                double recall = (double) tp / (tp + fn);
                //System.out.println("Precision: " + precision);
                //System.out.println("Recall: " + recall);
                //System.out.println("Fscore: " + 2 * precision * recall / (precision + recall));
            }
        }
        return predictions;
    }

    public static svm_model loadModel(String filePath) throws IOException, ClassNotFoundException {
        return svm.svm_load_model(filePath);
    }
    public static int[] predict2(Instance[] instances, svm_model model, boolean displayResult) throws IOException
    {
    	int[] predictions = new int[6];
    	for (int i=0;i<6;i++){
    		predictions[i]=0;
    	}
    	BufferedWriter bf = new BufferedWriter(new FileWriter(new File("summary.txt")));
    	for(Instance instance: instances){
    		double[] prob_est = new double[6];
    		svm.svm_predict_probability(model, new svm_node(instance.getData()), prob_est);
    		double mx=0.0;
    		int ind=0;
    		for(int i=0;i<6;i++){
    			if(prob_est[i]>mx){
    				mx=prob_est[i];
    				ind=i;
    			}
    			bf.write(prob_est[i]+" ");
    		}
    		predictions[ind]++;
    		bf.write("\n");
    	}
    	bf.close();
    	return predictions;
    }
}