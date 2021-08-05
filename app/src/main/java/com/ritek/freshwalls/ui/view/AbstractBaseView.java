package com.ritek.freshwalls.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public abstract class AbstractBaseView
        extends View
{
    public static String tag="AbstractBaseView";

    //Required if you are directly instantiating.
    //You could have more constructors with more arguments if you want
    //to consistently set the internal variables.
    public AbstractBaseView(Context context) {
        super(context);
    }

    //Called by the layout inflater.
    //Use TypedArray approach to read the custom attributes
    public AbstractBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //This is not called by the layout inflater but a derived class.
    //A derived view when invoked by the layout inflater may choose to
    //pass another attribute set reference from its theme whose
    //resource id is defStyle.
    public AbstractBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //Called by measure() of the base view class.
    //This method is called only if a layout is requested on this child.
    //Return after setting the required size through setMeasuredDimension
    //The default onMeasure from View may be sufficient for you.
    //But if you allow using wrap_conent for this view you may want to
    //override the wrap_content behavior which by default takes the entire space
    //supplied. The following logic implments this properly by overriding
    //onMeasure. This should be suitable for a number of cases, otherwise
    //you have all the logic here.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        logSpec(MeasureSpec.getMode(widthMeasureSpec));
        Log.d(tag, "size:" + MeasureSpec.getSize(widthMeasureSpec));

        setMeasuredDimension(getImprovedDefaultWidth(widthMeasureSpec),
                getImprovedDefaultHeight(heightMeasureSpec));
    }

    //Just a utility method to see what the spec is
    private void logSpec(int specMode)
    {
        if (specMode == MeasureSpec.UNSPECIFIED)
        {
            Log.d(tag,"mdoe: unspecified");
            return;
        }
        if (specMode == MeasureSpec.AT_MOST)
        {
            Log.d(tag,"mdoe: at msot");
            return;
        }
        if (specMode == MeasureSpec.EXACTLY)
        {
            Log.d(tag,"mdoe: exact");
            return;
        }
    }
    /*
     * This is called as part of layout() method from the base View class.
     * onLayout() will be called subsequently.
     * onLayout() is useful for view groups
     * oldw and oldh are zero if you are newly added.
     * View has already recorded the width and height
     * invalidate() is already active and called by the super class
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
    }

    //Called by layout() of the View class
    //By the time onLayout() is called the onSizeChanged
    //is already called by layout().
    //You typically override this when you are writing your own layouts.
    //Then you will call the layout() on your children.
    //
    //If you are customizing just a view you don't have to override this method.
    //parent onLayout() is empty.
    //
    //changed: this is a new size or position for this view
    //rest: postions with respect to the parent
    //
    @Override
    protected void onLayout (boolean changed, int left, int top, int right, int bottom)
    {
        Log.d(tag,"onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    //onDraw may not be called if you don't invalidate.
    //You can use the size of the view and don't have to
    //rely on onSizeChanged. Because by the time this method is called
    //the layout method has already set the sizes properly.
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(tag,"onDraw called");
    }

    //There are number of ways to save and restore state.
    //The recommended approach is for each dervied view to
    //do this. So it is hard to encapsulate that in a base class.
    //See CircleView for a pattern that uses BaseSavedData from
    //the base View class.
    @Override
    protected void onRestoreInstanceState(Parcelable p)
    {
        Log.d(tag,"onRestoreInstanceState");
        super.onRestoreInstanceState(p);
    }
    @Override
    protected Parcelable onSaveInstanceState()
    {
        Log.d(tag,"onSaveInstanceState");
        Parcelable p = super.onSaveInstanceState();
        return p;
    }

    /*
     * Unspecified:
     *     used for scrolling
     *     means you can be as big as you would like to be
     *     return the maximum comfortable size
     *     it is ok if you are bigger because scrolling may be on
     *
     * Exact:
     *     You have indicated your explicist size in the layout
     *     or you said match_parent with the parents exact size
     *     return back the passed in size
     *
     * atmost:
     * 	   I have this much space to spare
     *     sent when wrap_content
     *     Take as much as you think your natural size is
     *     this is a bit misleading
     *     you are advised not to take all the size
     *     you should be smaller and return a preferred size
     *     if you don't and take all the space, the other siblings
     *     will get lost.
     *     In this implementation I used the minimum size to satisfy
     *     atmost.
     *
     */
    private int getImprovedDefaultHeight(int measureSpec) {
        //int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return hGetMaximumHeight();
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                return hGetMinimumHeight();
        }
        //you shouldn't come here
        Log.e(tag,"unknown specmode");
        return specSize;
    }

    private int getImprovedDefaultWidth(int measureSpec) {
        //int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                return hGetMaximumWidth();
            case MeasureSpec.EXACTLY:
                return specSize;
            case MeasureSpec.AT_MOST:
                return hGetMinimumWidth();
        }
        //you shouldn't come here
        Log.e(tag,"unknown specmode");
        return specSize;
    }
    //Override these methods to provide a maximum size
    //"h" stands for hook pattern
    abstract protected int hGetMaximumHeight();
    abstract protected int hGetMaximumWidth();

    protected int hGetMinimumHeight()
    {
        return this.getSuggestedMinimumHeight();
    }
    protected int hGetMinimumWidth()
    {
        return this.getSuggestedMinimumWidth();
    }
}