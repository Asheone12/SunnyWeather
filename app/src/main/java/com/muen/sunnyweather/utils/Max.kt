package com.muen.materialdesigntest.utils

import java.lang.RuntimeException

fun<T:Comparable<T>> max(vararg nums:T):T{
    if(nums.isEmpty()) throw RuntimeException("Params can not be empty.")
    var maxNum=nums[0]
    for(num in nums){
        if(num >maxNum){
            maxNum=num
        }
    }
    return maxNum
   /* var maxNum=Int.MIN_VALUE        //整型范围内的最小数
    for(num in nums){
        maxNum=max(maxNum,num)
    }
    return maxNum*/
}

fun main() {
    val a=3.5
    val b=4.0
    val c=2.5
    val larger=max(a,b,c)
    println("larger is $larger")
}