/**
 * 
 */
package AFSA.algorithm;

import AFSA.domain.Fish;

/**
 * @author hba �˹���Ⱥ�㷨
 */
public interface AFSA {

	void initPop();

	void start() throws Exception;

	Fish AF_Prey(Fish fish);

	Fish AF_Follow(Fish fish);

	Fish AF_Swarm(Fish fish);

}
