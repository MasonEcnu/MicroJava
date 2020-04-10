package com.mason.stage_one.chapter_one.section_three.source;

/**
 * Created by WM on 2020/4/4
 *
 * @author Doug Lea
 * @since 1.5
 */
public interface ReadWriteLock {
    /**
     * Returns the lock used for reading.
     *
     * @return the lock used for reading
     */
    Lock readLock();

    /**
     * Returns the lock used for writing.
     *
     * @return the lock used for writing
     */
    Lock writeLock();
}