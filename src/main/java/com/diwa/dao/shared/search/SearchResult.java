package com.diwa.dao.shared.search;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa al Resultado de una búsqueda. Este resultado podrá ser tanto paginada como no paginada.
 * <p>
 * En el caso de que la búsqueda sea paginada, se devolverá una lista con los elementos que correspondía al segmento
 * solicitado y se indicará el número total de elemento que hay en la BD que coinciden con los criterios de búsqueda
 * establecidos.
 * <p>
 * 
 * @param <T> Clase de los elementos que componen el listado
 */
public class SearchResult<T> implements Serializable {

    /**
     * serial versio uid.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Elementos resultantes de realizar la búsqueda en BD.
     */
    private List<T> elements = new ArrayList<T>();

    /**
     * Número de elementos que hay en BD y coinciden con los criterios de búsqueda establecidos (número de resultados).
     * En el caso de que este atributo sea igual a <code>elements.size()</code> indicará que la búsqueda realizada no
     * era paginada o que los resultados caben en una sola página. Por defecto el resultado no es paginado.
     */
    private long totalMatches = -1;

    /**
     * Constructor por defecto.
     */
    public SearchResult() {
    }

    /**
     * Constructor.
     * 
     * @param elements Elementos resultantes de realizar la búsqueda en BD
     */
    public SearchResult(final List<T> elements) {
        this(elements, elements.size());
    }

    /**
     * Constructor.
     * 
     * @param elements Elementos resultantes de realizar la búsqueda en BD
     * @param totalMatches Número de elementos que hay en BD y coinciden con los criterios de búsqueda establecidos.
     */
    public SearchResult(final List<T> elements, final long totalMatches) {
        this.elements = elements;
        this.totalMatches = totalMatches;
    }

    /**
     * @return Elementos resultantes de realizar la búsqueda en BD.
     */
    public List<T> getElements() {
        return elements;
    }

    /**
     * @param elements Elementos resultantes de realizar la búsqueda en BD.
     */
    public void setElements(final List<T> elements) {
        this.elements = elements;
    }

    /**
     * @return Número de elementos que hay en BD y coinciden con los criterios de búsqueda establecidos.
     */
    public long getTotalMatches() {
        return totalMatches;
    }

    /**
     * @param totalMatches Número de elementos que hay en BD y coinciden con los criterios de búsqueda establecidos.
     */
    public void setTotalMatches(final long totalMatches) {
        this.totalMatches = totalMatches;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elements, totalMatches);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SearchResult<T> other = (SearchResult<T>) obj;
        if (elements == null) {
            if (other.elements != null)
                return false;
        } else if (!elements.equals(other.elements))
            return false;
        if (totalMatches != other.totalMatches)
            return false;
        return true;
    }
}
