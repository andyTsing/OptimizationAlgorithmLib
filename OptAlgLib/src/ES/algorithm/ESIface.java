package ES.algorithm;

import java.util.List;

import ES.domain.Individual;

public interface ESIface {
	/**
	 * �㷨����
	 * 
	 * @throws Exception
	 * 
	 */
	public void start() throws Exception;

	/**
	 * ��ʼ����Ⱥ
	 */
	public void initPop();

	/**
	 * ���ݸ��������Ӵ�
	 * 
	 * @param population
	 * @return
	 */
	public List<Individual> makeNewIndividuals(List<Individual> population);

	/**
	 * ����Ⱥ���Ƴ����Ƹ���
	 * 
	 * @param population
	 * @return
	 * @throws Exception
	 */
	public Individual killBadIndividuals(List<Individual> population) throws Exception;
}
