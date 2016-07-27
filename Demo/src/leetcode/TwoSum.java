package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mercy
 *Example:
 *Given nums = [2, 7, 11, 15], target = 9,
 *Because nums[0] + nums[1] = 2 + 7 = 9,
 *return [0, 1].
 */
public class TwoSum {
	public static void main(String[] args) {
		int[] nums={2,3,4,5,6,7,9};
		int target=12;
		int[] arr=twoSum2(nums,target);
		System.out.println(arr[0]+"--"+arr[1]);
		List<Integer> list=twoSum3(nums,target);
		for(Integer i:list){
			System.out.println(i);
		}
		
	}
	public static int[] twoSum(int[] nums, int target) {
        return new int[] {1,1};
    }
	/**
	 * @param nums
	 * @param target
	 * @return
	 * 用Map方法
	 * @author mercy
	 */
	public static int[] twoSum1(int[] nums, int target) {
        return new int[] {1,1};
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
	
	/**
	 * @param nums
	 * @param target
	 * @return
	 * 输出多个List
	 * @author mercy
	 */
	public static List<Integer> twoSum3(int[] nums, int target) {
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<nums.length;i++){
			for(int j=i+1;j<nums.length;j++){
				if(nums[j]==target-nums[i]){
					list.add(i);
					list.add(j);
				}
			}
		}
		return list;
    }
	
	/**
	 * @param nums
	 * @param target
	 * @return
	 * 输出多个Map
	 * @author mercy
	 */
	public static List<Integer> twoSum4(int[] nums, int target) {
		List<Integer> list=new ArrayList<Integer>();
		for(int i=0;i<nums.length;i++){
			for(int j=i+1;j<nums.length;j++){
				if(nums[j]==target-nums[i]){
					list.add(i);
					list.add(j);
				}
			}
		}
		return list;
    }

}
