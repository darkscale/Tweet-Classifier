package SVM;


import SVM.utils.DataFileReader;
import SVM.ca.uwo.csd.ai.nlp.kernel.KernelManager;
import SVM.ca.uwo.csd.ai.nlp.kernel.LinearKernel;
import SVM.ca.uwo.csd.ai.nlp.libsvm.svm_model;
import SVM.ca.uwo.csd.ai.nlp.libsvm.ex.Instance;
import SVM.ca.uwo.csd.ai.nlp.libsvm.ex.SVMPredictor;
import TweetsRefine.IndexCreatorTest;

public class LoadModel
{
	public static int arr[][];
	static double[] classCount;
	static String class_array[]={"Business","Music","Health","Politics","Sports","Technology"};
	
	public static double[] loadModel() throws Exception
	{
		KernelManager.setCustomKernel(new LinearKernel());
		Instance[] testingInstances = DataFileReader.readDataFile("./svm/"+"test.txt");
		svm_model model=SVMPredictor.loadModel("./svm/model/model.model");
		int [] lt= SVMPredictor.predict2(testingInstances, model, true);
		double[] result= new double[6];
		for(int i=0;i<6;i++){
			result[i]=(double)lt[i];
		}
		return result;
	}
	
	public static void main(String arr[]) throws Exception
	{
		IndexCreatorTest.createIndex("test", "svm");
		double[] classifyClass=loadModel();
		System.out.println("Overview of Tweets -> \n");
		for(int i=0;i<classifyClass.length;i++){
			System.out.println(class_array[i]+" => "+classifyClass[i]);
		}
	}
}
