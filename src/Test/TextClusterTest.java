package Test;

import java.util.List;

import org.haikism.textcluster.ToCluster;
import org.haikism.textcluster.Tokeniser;

public class TextClusterTest {

	public static void main(String[] args) throws Exception {
		ToCluster cs = new ToCluster(100, "res/input_textCluster_test3.txt",6,"res/keyWords_sentences_cluster.txt",3);
//		cs.dataPreProcess_weibo();
		cs.startCluster();
		
//		for (int i = 2; i < 150; i++) {
//			System.out.println("输入的个数："+i+"  输出的个数："+cs.func(i));
//		}
		
		
//		List<String> st=new Tokeniser().partition("【不动产登记提速？】国土部可能改变思路，取消不动产登记与房产税挂钩，以加快");
//		for (int i = 0; i < st.size(); i++) {
//			System.out.println(st.get(i)+"  ");
//		}
	}

}
