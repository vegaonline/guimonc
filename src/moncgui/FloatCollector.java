/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.util.Arrays;

/**
 *
 * @author vega
 */
public class FloatCollector {
    
    private float[] curr=new float[64];
    private int size;

    public FloatCollector(){}
    
    public FloatCollector(int initialSize){
        if(curr.length<initialSize){
            curr=Arrays.copyOf(curr, initialSize);
        }
    }
    public void add(double d) {
        if(curr.length==size){
            curr=Arrays.copyOf(curr, size*2);
        }
        curr[size++]=(float)d;
    }

    public void join(FloatCollector other) {
        if(size+other.size > curr.length) {
            curr=Arrays.copyOf(curr, size+other.size);
        }
        System.arraycopy(other.curr, 0, curr, size, other.size);
        size+=other.size;
    }

    public float[] toArray() {
        if(size!=curr.length){
            curr=Arrays.copyOf(curr, size);
        }
        return curr;
    }
}
