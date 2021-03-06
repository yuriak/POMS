package Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import libsvm.svm;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Term;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.haikism.*;
import org.haikism.LIBSVM.AnalysisBaseOnLIBSVM;
import org.haikism.config.FileOperation;

public class baseTest {

	/**
	 * 目录：
	 * libs：存放用到的jar包
	 * res：存放预测结果数据，模型文件，情感词库等
	 * 
	 * 使用方法：
	 * 1.训练，获得模型 
	 * 这个测试文件中的testSentence字符串数组是用于输入用户评论内容的，可以从数据库中查找评论，然后放到这个数组里面，
	 * 然后通过ConvertToLIBSVMData对象将所有评论逐条分词、在情感词库中查找得到每个词的倾向值、生成用于训练的文件（默认是trainData.txt），
	 * 接下来需要修改生成的trainData.txt，每行的第一个数字代表对应的testSentence中的评论是消极或是积极，这里用1代表积极，-1代表消极，
	 * 需要手动修改这个值，（生成的时候我都用1来表示了）
	 * 2.用训练好的模型预测数据
	 * （1）如果只是单纯为了测试，可以直接把刚才的trainData.txt复制成testData.txt，然后用AnalysisBaseOnLIBSVM对象的train方法进行预测，
	 *    预测结果文件默认名称是predictData.txt，这个方法会返回一个精度值，即：预测结果文件中判断正确的条数/总评论数
	 *    （也可以找另一批数据，然后再对他们进行一遍手工标注，麻烦了点）
	 * （2）正常使用的情况下，应该是通过历史数据训练好模型，然后进行分析的时候不用再去训练模型，直接通过ConvertToLIBSVMData对象生成用于预测的文件（第二个参数是文件名），
	 *    然后使用AnalysisBaseOnLIBSVM对象的train方法根据生成的文件和给定了的模型进行预测，
	 *    需要注意的是，此时精度值不再有意义，因为用于测试的文件没有经过人工标注，也就无法判断结果是否准确
	 * 
	 * 注：1.libs里面的ansj_seg-2.0.7.jar和nlp-lang-0.3.jar是用于分词的jar包（导入即可用），但是他还提供了识别关键字等功能，
	 *    也许你在检索各网站关于某一类新闻的时候可以用到，开发文档的地址：http://nlpchina.github.io/ansj_seg/
	 *    2.这是个最基本的测试版，还有许多工作没有做，因此精度并不是很高（也可能和我训练时用的评论太少有关），
	 *    如果没有其他需求的话，往后我的工作主要是通过加入新的词库或者添加一些调整感情倾向值的方法等来提高预测精度，但是类所提供的主要功能对使用者应该是不变的，
	 *    所以我们可以在这些功能的基础上尽快把平台搭建出来，如果类的方法什么的你用起来有哪些不方便那你告诉我，我再改，或者你改完了告诉我也行。
	 *    3.最好尽早确定我们用到哪些网站，然后对他们的评论进行分类，比如新闻的一类，论坛的一类，或者政治事件的一类、社会事件的一类这种，
	 *    然后搜集历史数据，让我对象和向榕去手工标注每个评论的正负感情倾向，这个工作越早做越好，因为按理说应该是历史数据越多获得的模型越精确。
	 */
	
	public static void main(String[] args) {
		//测试数据来源：豆瓣电影短评，前20条数据是积极的，后20条数据是消极的
//		String []testSentence={
//				//消极
//				 "-1/#HKIFF#2015。这是什么玩意？又名《汪星人崛起》之惊变28天音乐改变狗生？毫无逻辑的学生作品剧本，手持镜头晃的想吐，表演0分的女主以及人类，狗狗倒是演技不俗。主人公狗你能一步步找到仇人复仇你敢不敢先找回家啊？唯一的萌点是那只白色的狗虽然最后还是没用。只想无限地喷。"
//				,"-1/家里有三只爱犬的我都觉得太做作了...垃圾，无聊"
//				,"-1/#HKIFF#电影节目前为止看到最垃圾的电影。导演明显拍狗比拍人更用心，对狗导戏比对人更擅长，居然还找了个面瘫来演女主角，中二的表演真是让人哭笑不得。电影从人狗分离开始就变成《猩球崛起》狗狗版，配乐剪辑拍法越来越好莱坞，可惜那种童话式的文艺稚气始终未脱，越来越狗血，越演越扯蛋。  iPhone"
//				,"-1/快进着看了一下，预计我会给一颗星。纯主观讨厌虐狗。"
//				,"-1/技术没问题，但观感不适，狗狗不是你们玩弄的道具。"
//				,"-1/HKIFF】拍得硬邦邦，父女间的互动尤其莫名，似乎一切摹写就是为了冲着结局那幅画面去的，消极，悲观，无聊"
//				,"-1/开篇弄那个牛把我恶心的不要不要，后面简直血腥的怕做噩梦。还有为什么爸爸会亲自给女儿穿鞋？"
//				,"-1/陈词滥调，煽情做作，故事毫无新意，手法故弄玄虚。看开头知结尾，陈芝麻烂谷子。人的戏凌乱且不知所云，故事编的一塌糊涂。到处都是逻辑漏洞和随意的转折。"
//				,"-1/为什么大家要演这种烂片,无趣，不开心，恶心，单调"
//				,"-1/期待了足足有半年，预告里面就是所有的精彩镜头了，感觉把所有的钱都花到那几场不怎么激烈的动作戏里面了，End of Watch里面的真实感也没保持，剧本也写得不好太碎，只有两个镜头有意思其他都不咋样，说阿诺突破自己，并没有，虽然不愿意承认，但是演得太差了……还是get to the chopppaaaa吧……"
//				,"-1/哦 不对 这简直没有剧情，没有逻辑，没有故事....... 完全不知道发生了什么，只有暴力吧大概..... 同事还因为应该看电影时喝什么饮料，差点和我打起来 ;)"
//				,"-1/虽然有施瓦辛格的加入也不能挽救二流影片的命运，破坏者除了枪战没有一点动作镜头，没有了施瓦辛格招牌式的出演相信没几个人会觉得他是动作明星，二流的商业片不注重剧情和镜头感，就连最激烈的枪战也让人提不起精神，这样的影片在美国应该是属于中下等的水平，想看阿诺的人还是死了心吧。"
//				,"-1/情节疏漏、悬念无果、节奏松弛错乱、貌似业余的动作指导，作为已有所成就的导演与编剧合作之下的产物，竟然是这个样子，实在让人大跌眼镜，比last stand差了好多。浪费了Enos那么好的演技。"
//				,"-1/这电影是烂，平淡无奇，主线不吸引人。"
//				,"-1/我想说我几乎都没耐心看完了好吗···反正就是无聊的打打杀杀。"
//				,"-1/其实这部片里面除了斯瓦辛格还是有大牌的，比如那个开雪佛兰的黑人。。。但是影片实在是不敢恭维。。。应该是我看过斯瓦辛格演过影片里面最难看的片吧。。。衔接实在是太差了。。。老大为报仇自己黑了钱不说，引起内乱，背后下黑手居然是女队友。。。整个故事被导演讲得一塌糊涂。。。"
//				,"-1/烂片，垃圾，不够精彩"
//				,"-1/杨幂,生活中是个不好笑的自黑段子手,工作上是个零演技的演员.本质上是个不好看的花瓶.AB和黄二明在生活中不像情侣,在戏里不像兄妹,归根到底:两人的演技都很屎.大家别再笑四娘作家也能当导演,这部戏的导演是教主经纪人,感觉烂片的下限又被拉低了.教主是投资人,恋情炒得再凶,我就不看就不给你们凑分子钱."
//				,"-1/拍的稀巴烂，演的稀巴烂。简言之，这是一部会让所有<讨厌黄晓明 杨幂 ab等演员及杨文军等主创>的人看了开心的电影。看到你们这么不思进取，我就放心了。"
//				,"-1/《何以笙箫默》彻底打破了电影自身的桎梏，肆意泼墨的跳跃性剧情，无需逻辑的人设关系，导演编剧毅然认定了所有人都以将本自小说的情景烂熟于心，再辅之足够亮瞎双眸的超强柔光预以突显该片演员那异乎常人的演技，宛同预告式的电影，无疑开创了渣作模式的新纪元。"
//				//积极
//				,"1/精彩之至。让人记住太多的面孔。"
//				,"1/真是好看死了，三个我大爱的爷们彪戏，没有比这更爽的了，华丽时代背景+完美叙事剪辑，从情节到细节，从演技到设计，无可挑剔。"
//				,"1/bud太牛逼了！真是条汉子！，好厉害"
//				,"1/就爱那个年代的美国，英雄，喜欢"
//				,"1/黑脸白脸，goog cop bad cop。精彩到位，错综有致，足够有劲的铁三角警匪悬疑片。"
//				,"1/曲折的悬疑剧情，纯属的多线叙事，最最重要的是三个独具魅力的警察形象。"
//				,"1/往往隐藏最深的才是最邪恶的…看过最牛逼的警匪片，凯文斯派西卢塞尔克劳盖皮尔斯三大男主角实在没话说，性格各异大飙演技，还未成大牌的时候表演就如此有张力…故事环环相扣，案中案却别有洞天，人物复杂但都对故事有推动作用…小说故事设计巧妙，改编的也成功，不得而知是否纯粹真实事件还是又经加工"
//				,"1/乱象丛生的黑色年代，错综复杂的敌我交锋；实在令人赞叹到咋舌。"
//				,"1/多线叙事，一个相当复杂的故事，环环相扣，虽没有太多震撼但却足够严谨，通过警官的视点揭示了美国社会复杂的利益关系，同时也对人性对“正义”做出了解读，经典作品。"
//				,"1/果然是经典，返回头看现在很多电影都有这部片子的影子。"
//				,"1/如此完美的剧情片会永久载入电影史。 对话和情节交叉暗含深意，彼此映照，令人在最后的恍然大悟之际瞬间闪回前剧细节。 当时想当刑警的Ed虽然想都没想就承认自己不会在嫌犯背后开枪以免他的律师最终帮他脱罪，但是最终当现实情景到来的关头，他却想都没有想就坚定的朝嫌犯的背后开了枪。"
//				,"1/丝丝入扣的复杂而完美的剧情，几大影帝嘉年华似的表演，完美的诠释了警察内部的黑暗与斗争，本片在同类型的电影题材中，绝对是可以排进前十名的电影"
//				,"1/好久没看好莱坞电影了，故事精巧，剧本扎实，运镜流畅，警匪片中的优质片目！PS：凯文·史派西和罗素·克劳我真心不太分得清谁是谁啊。。"
//				,"1/以前我最喜欢罪恶之城里老布的Magnum……不过L.A.P.D.之后我更喜欢Russell的M1霰弹枪了！！！"
//				,"1/思维缜密逻辑清晰，你妈罗素克劳我刚看出硬汉啊 出去打埃斯利那段简直跟丧尸一样 挺好看的戳穿一个惊天大阴谋！！"
//				,"1/好剧本和强大的演员阵容。"
//				,"1/这阵容，想不好看都难。kevin spacey被打穿胸口的镜头翻来覆去看了几遍，真你妈好演员。公众形象永远高于人民知道真相的权利，在哪又不是这样呢？"
//				,"1/剧情相当之连贯，看起来一气呵成，结局也很有颠覆意味"
//				,"1/本片并不复杂，三个警察，一个油滑，一个暴力，一个智慧，刚开始格格不入，却因为一个案件的蹊跷卷在了一起，发现了警察局副局长贩毒以及想要接管黑社会的内幕，最后拳头赢得了从良的妓女，智慧赢得全世界，只因他们都有一个信念，罗罗托马斯，正义！影片的前半部分实在精彩，不拖沓还能交代三人性格"
//				,"1/好莱坞版的两杆大烟枪．这些日后的超级实力派大碗们合伙为我们奉献了一场经典的警匪电影！"
//		};
		
		String[] trainSentence=FileOperation.readTxtFile(new File("res/input_LIBSVM_test1")).split("\r\n");

		
		String[] trainArgs={"res/LIBSVM_tmp","res/LIBSVM.model"};
		String[] predictArgs={"res/LIBSVM_tmp","res/LIBSVM.model","res/LIBSVM_predict.txt"};
		AnalysisBaseOnLIBSVM svm= new AnalysisBaseOnLIBSVM(trainSentence,trainArgs,predictArgs,false);
		svm.train();
		
		
		
		
		
		//预测
		System.out.println("预测的精度是："+svm.predict()); 
		
		
//		String[] predictArgs={"res/LIBSVM_tmp","res/LIBSVM.model","res/LIBSVM_predict.txt"};
//		System.out.println("预测的精度是："+new AnalysisBaseOnLIBSVM().predict_fortest(predictArgs));
		
		


	}

}
