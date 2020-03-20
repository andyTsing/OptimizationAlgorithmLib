package ABC.algorithm;

import java.util.List;

import ABC.domain.FoodSource;

public interface ABCIface {
	/**
	 * ��Ⱥ��ʼ��
	 */
	void initPop();

	/**
	 * ��ʼ
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * ��Ӷ��׶�
	 * 
	 * @param foodSources
	 */
	void employeeBeePhase(List<FoodSource> foodSources);

	/**
	 * ����׶�
	 * 
	 * @param foodSources
	 */
	void scoutBeePhase(List<FoodSource> foodSources);

	/**
	 * �۲��׶�
	 * 
	 * @param foodSources
	 */
	void onlookerBeePhase(List<FoodSource> foodSources);

}
