package TweetsRefine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class IndexBuildTest 
{
	
	String fileName="";
	public List<String> listOfUniqWord=new ArrayList<String>();
	
	public void readUniqueWord(String modelName) throws IOException
	{
		BufferedReader br;
		if(modelName=="svm"){
			br=new BufferedReader(new FileReader(new File("uniqueWord_svm")));
		}
		else{
			br=new BufferedReader(new FileReader(new File("uniqueWord")));
		}
		
		String str="";
		while((str=br.readLine()) != null)
		{
			listOfUniqWord.add(str);
		}
	}
	
	public void createIndexSVM(String fileName) throws IOException
	{
		//int count=0;
		BufferedWriter bw_index=new BufferedWriter(new FileWriter(new File("./svm/"+"test.txt")));
		BufferedReader br_index=new BufferedReader(new FileReader(new File(fileName)));
		String str="";
		int treeIndex;
		boolean flag;
		String className="-1";
		while((str=br_index.readLine()) != null)
		{
			//count++;
			flag=true;
			//1.//List<Integer> list=new ArrayList<Integer>(Collections.nCopies(listOfUniqWord.size(),0));
			NavigableMap<Integer,Integer> lt= new TreeMap <Integer,Integer>();
			String split[]=str.split(" ");
			for(int i=0;i<split.length;i++)
			{
				if(listOfUniqWord.contains(split[i]))
				{
					treeIndex=listOfUniqWord.indexOf(split[i]);
					if(lt.containsKey(treeIndex)==true){
						Integer x= lt.get(treeIndex);
						lt.remove(treeIndex);
						lt.put(treeIndex, x+1);
					}
					else{
						lt.put(treeIndex,1);
					}
				}
			}
			for(Map.Entry<Integer,Integer> entry: lt.entrySet()){
				if(flag){
					flag=false;
					bw_index.write(className);
				}
				Integer key=entry.getKey();
				Integer value=entry.getValue();
				bw_index.write(" "+key+":"+value);
			}
			if(!flag){
				bw_index.write("\n");
			}
		}
		bw_index.close();
		br_index.close();
	}
	
	public void fixedArffAttributeOfNaive() throws IOException
	{
		BufferedWriter bw=new BufferedWriter(new FileWriter(new File("./naive/"+"test.arff")));
		bw.write("\n@RELATION iris\n");
		for(int i=0;i<listOfUniqWord.size();i++)
		{
			if(!listOfUniqWord.get(i).equals("class"))
				bw.write("@ATTRIBUTE "+listOfUniqWord.get(i)+"	REAL\n");
			else
				bw.write("@ATTRIBUTE "+"clas"+"	REAL\n");
		}
		bw.write("@ATTRIBUTE class 	{business,music,health,politics,sports,technology}\n@DATA\n");
		bw.close();
	}
	
	public void createIndexNaive(String fileName) throws IOException
	{
		BufferedWriter bw_index=new BufferedWriter(new FileWriter(new File("./naive/"+"test.arff"),true));
		BufferedReader br_index=new BufferedReader(new FileReader(new File(fileName)));
		String str="";
		int treeIndex;
		int listIndex;
		while((str=br_index.readLine()) != null)
		{
			List<Integer> list=new ArrayList<Integer>(Collections.nCopies(listOfUniqWord.size(),0));
			String split[]=str.split(" ");
			for(int i=0;i<split.length;i++)
			{
				if(listOfUniqWord.contains(split[i]))
				{
					treeIndex=listOfUniqWord.indexOf(split[i]);
					listIndex=list.remove(treeIndex);
					list.add(treeIndex,++listIndex);
				}
			}
			bw_index.write("{");
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i) != 0)  
				{
					bw_index.write(i+" "+list.get(i)+",");
				}
			}
			bw_index.write(listOfUniqWord.size()+" "+"?"+"}"+"\n");
			list.clear();
		}
		bw_index.close();
		br_index.close();
	}
}
