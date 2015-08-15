package Test;

import SubObjAnalysis.SetThresholdValue;

public class SubObjTest {

	public static void main(String[] args) {
//		SubObj_train so=new SubObj_train();
//		new SubObj_predict(1.0);
		SetThresholdValue setThresholdValue=new SetThresholdValue(1, 0.05, "res/trainData_SubObjAnalysis.txt");
		System.out.println(setThresholdValue.getAccuracy());
//		HashMap<String, Double> s=new HashMap<String, Double>();
		
//		Map s= new HashMap();
//		
//		s.put("a",1.2);
//		
////		System.out.println(((Double)s.get("a")).doubleValue());
//		System.out.println(s.get("a"));
	}

}
