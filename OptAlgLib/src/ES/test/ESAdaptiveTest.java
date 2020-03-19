package ES.test;

import Common.ObjectiveFun;
import ES.algorithm.ESAdaptive;
import ES.operation.RecombinationIntermediate;

public class ESAdaptiveTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// �½�Ŀ�꺯��
		ObjectiveFun objectiveFun = new ObjectiveFun();
		ESAdaptive es = ESAdaptive.builder().popNum(20).crossNum(20).childNum(100).concatParentandChild(true)
				.maxGen(500).dim(2).recombination(new RecombinationIntermediate()).objectiveFun(objectiveFun).build();
		es.start();
	}
}
