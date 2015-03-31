package com.diwa.dao.search;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;

/**
 * Wrapper de la clase ScrollResult. Facilita la conversión de tipos y la obtención de los datos.
 * 
 * @param <T> Clase de los elementos que componen el listado
 */
public class ScrollResult<T> {
    /**
     * Entidad envuelta. Delegamos en ella
     */
    private ScrollableResults entity;

    /**
     * Constructor.
     * 
     * @param e Elementos resultantes de realizar la búsqueda en BD
     */
    public ScrollResult(final ScrollableResults e) {
        entity = e;
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#next()
     */
    public boolean next() throws HibernateException {
        return entity.next();
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#previous()
     */
    public boolean previous() throws HibernateException {
        return entity.previous();
    }

    /**
     * @param i
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#scroll(int)
     */
    public boolean scroll(int i) throws HibernateException {
        return entity.scroll(i);
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#last()
     */
    public boolean last() throws HibernateException {
        return entity.last();
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#first()
     */
    public boolean first() throws HibernateException {
        return entity.first();
    }

    /**
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#beforeFirst()
     */
    public void beforeFirst() throws HibernateException {
        entity.beforeFirst();
    }

    /**
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#afterLast()
     */
    public void afterLast() throws HibernateException {
        entity.afterLast();
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#isFirst()
     */
    public boolean isFirst() throws HibernateException {
        return entity.isFirst();
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#isLast()
     */
    public boolean isLast() throws HibernateException {
        return entity.isLast();
    }

    /**
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#close()
     */
    public void close() throws HibernateException {
        entity.close();
    }

    /**
     * @param i
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#get(int)
     */
    @SuppressWarnings("unchecked")
    public T get() throws HibernateException {
        return (T) entity.get(0);
    }

    /**
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#getRowNumber()
     */
    public int getRowNumber() throws HibernateException {
        return entity.getRowNumber();
    }

    /**
     * @param rowNumber
     * @return
     * @throws HibernateException
     * @see org.hibernate.ScrollableResults#setRowNumber(int)
     */
    public boolean setRowNumber(int rowNumber) throws HibernateException {
        return entity.setRowNumber(rowNumber);
    }
}
