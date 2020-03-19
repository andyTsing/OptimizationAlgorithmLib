package FA.algorithm;

import java.awt.font.NumericShaper.Range;

/**
 * ө����㷨�ӿ�
 * 
 * @author hba
 *
 */
public interface FireflyAlgorithm {
	/**
	 * ��Ⱥ��ʼ��
	 */
	public void initPop();

	/**
	 * ��������
	 */
	public void calcuLight();

	/**
	 * ����ŷ�Ͼ���
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double calcDistance(double[] a, double[] b);

	/**
	 * ө����ƶ�
	 */
	public void fireflyMove();

	/**
	 * �㷨����
	 * 
	 */
	public void start();

}
