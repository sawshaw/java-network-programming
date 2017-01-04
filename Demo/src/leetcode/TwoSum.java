package leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mercy
 *Example:
 *Given nums = [2, 7, 11, 15], target = 9,
 *Because nums[0] + nums[1] = 2 + 7 = 9,
 *return [0, 1].
 */
public class TwoSum {
	public static void main(String[] args) {
		int[] nums={2,0,4,9,5,7,10,9};
		int target=12;
		int[] arr=twoSum1(nums,target);
		System.out.println(arr[0]+"--"+arr[1]);
	}
	public static int[] twoSum(int[] nums, int target) {
		Map<Integer,Integer> map=new HashMap<>();
		for(int i=0;i<nums.length;i++){
			map.put(nums[i], i);
		}
		for(int i=0;i<nums.length;i++){
			int other=target-nums[i];
			if(map.containsKey(other)&&map.get(other)!=i){
				 return new int[] { i, map.get(other) };
			}
		}
		throw new IllegalArgumentException("No two sum solution");
    }
	/**
	 * @param nums
	 * @param target
	 * @return
	 * 用Map方法
	 * @author mercy
	 */
	public static int[] twoSum1(int[] nums, int target) {
		Map<Integer,Integer> map=new HashMap<>();
		for(int i=0;i<nums.length;i++){
			int other=target-nums[i];
			if(map.containsKey(other)){
				return new int[]{map.get(other),i};
			}
			map.put(nums[i], i);
		}	
		throw new IllegalArgumentException("No two sum solution");
    }
	
	/**
	 * @param nums
	 * @param target
	 * @return
	 * 传统的方法
	 * @author mercy
	 */
	public static int[] twoSum2(int[] nums, int target) {
		for(int i=0;i<nums.length;i++){
			for(int j=i+1;j<nums.length;j++){
				if(nums[j]==target-nums[i]){
					return new int[] {i,j};
				}
			}
		}
		throw new IllegalArgumentException("No two sum solution");
    }

}
