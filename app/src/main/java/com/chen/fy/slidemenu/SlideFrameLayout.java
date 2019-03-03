package com.chen.fy.slidemenu;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SlideFrameLayout extends FrameLayout {

    private View tv_context;
    private View tv_menu;

    private int contextWidth;
    private int menuWidth;
    private int viewHeight;

    /**
     * 滚动器
     */
    private Scroller scroller;

    public SlideFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contextWidth = tv_context.getMeasuredWidth();
        menuWidth = tv_menu.getMeasuredWidth();
        viewHeight = getMeasuredHeight();    //高,直接获取父窗体的高即可
    }

    /**
     * 当布局文件加载完成后回调这个方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_context = getChildAt(0);
        tv_menu = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        tv_menu.layout(contextWidth, 0, contextWidth + menuWidth, viewHeight);
    }

    private float startX;
    private float sX;   //水平滑动的距离
    private float sY;   //竖直滑动的距离

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                sX = event.getX();
                sY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float distanceX = endX - startX;
                int toScrollX = (int) (getScrollX() - distanceX);
                if (toScrollX < 0) {    //以初始状态为参照物,toScrollX小于0为往右边滑动,大于0为往左边滑动
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());
                float dX = Math.abs(endX - sX);
                float dY = Math.abs(endY - sY);
                if (dX > dY && dX > 8) {
                    //当水平滑动大于竖直滑动时
                    //进行事件反拦截,即SlideFrameLayout获取事件使用权
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                int totalScrollX = getScrollX();   //偏移量
                if (totalScrollX < menuWidth / 2) {
                    //关闭Menu
                    closeMenu();
                } else {
                    //打开Menu
                    openMenu();
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                sX = event.getX();
                sY = event.getY();
                if (slideChangeListener != null) {
                    slideChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                float dX = Math.abs(endX - sX);
                float dY = Math.abs(endY - sY);
                if (dX > 8) {
                    //当水平方向有滑动时,进行拦截事件,点击事件不可以出发
                    intercept = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return intercept;
    }

    public void openMenu() {
        int distanceX = menuWidth - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        invalidate();   //强制刷新
        if (slideChangeListener != null) {
            slideChangeListener.onOpen(this);
        }
    }

    public void closeMenu() {
        int distanceX = 0 - getScrollX();
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, getScrollY());
        invalidate();   //强制刷新
        if (slideChangeListener != null) {
            slideChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    private onSlideChangeListener slideChangeListener;

    /**
     * 监听SlideLayout状态的改变
     */
    public interface onSlideChangeListener {
        void onOpen(SlideFrameLayout slideFrameLayout);

        void onClose(SlideFrameLayout slideFrameLayout);

        void onDown(SlideFrameLayout slideFrameLayout);
    }

    public void setSlideChangeListener(onSlideChangeListener slideChangeListener) {
        this.slideChangeListener = slideChangeListener;
    }
}
