package com.diwa.dao.utils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Contiene un conjunto de métodos que son de utilidad para la generación de la consultas que van a ser generadas.
 */
public final class DaoUtils {
    /**
     * Contructor privado.
     */
    private DaoUtils() {
    }

    /**
     * Proporciona el nombre lógico de la entidad asociada a la clase indicada.
     *
     * @param type Clase del dominio para la que queremos obtener el nombre lógico establecido. Por defecto será el
     *            nombre de la clase, a no ser que sea indicado en la notación JPA @Entity.
     * @return Nombre lógico de la entidad del dominio.
     */
    public static String getEntityName(final Class< ? > type) {
        Entity entity = type.getAnnotation(Entity.class);
        if (entity == null) {
            return type.getSimpleName();
        }
        String entityName = entity.name();
        if (entityName == null || entityName.length() == 0) {
            return type.getSimpleName();
        } else {
            return entityName;
        }
    }

    /**
     * Indica si la entidad tiene un Id asociado. Un atributo con la notación @Id.
     *
     * @param <T> Tipo genérico al que pertenece la entidad del dominio
     * @param entity Objeto de una entidad del dominio
     * @return Si la entidad tiene un Id asociado
     */
    public static <T> boolean hasId(final T entity) {
        try {
            Field field = getDeclaredField(entity, getPrimaryKeyName(entity));
            field.setAccessible(true);
            Object propertyValue = field.get(entity);
            if (propertyValue == null) {
                // No id was found returning false
                return false;
            }
            if (propertyValue instanceof Number) {
                boolean isPrimitive = field.getType().isPrimitive();
                Number number = (Number) propertyValue;
                if (isPrimitive && number.longValue() <= 0) {
                    // An id was found and it was a number, but it was less than
                    // zero returning false
                    return false;
                }
                // An id was found and it was a number returning true
                return true;
            }
        } catch (NoSuchFieldException ex) {
            return false;
        } catch (IllegalAccessException ex) {
            return false;
        }
        return false;
    }

    /**
     * Proporciona el nombre lógico de la clave primaria correspondiente a la entidad del dominio pasada.
     *
     * @param type Clase del dominio para la que queremos obtener el nombre lógico de la PK.
     * @return Nombre lógico de la clave primaria
     */
    public static String getPrimaryKeyName(final Class< ? > type) {
        String pkName = findFieldsForPK(type);
        if (null == pkName) {
            pkName = findMethodsForPK(type);
        }
        return pkName;
    }

    /**
     * Proporciona El nombre del atributo correspondiente a la PK de la entidad.
     */
    private static <T> String getPrimaryKeyName(final T object) {
        return getPrimaryKeyName(object.getClass());
    }

    /**
     * Devuelve el nombre del atributo que tiene al anotación @Id. En el caso de que ningún atributo de la clase tenga
     * esta anotación devolverá null.
     */
    private static String findFieldsForPK(final Class< ? > type) {
        String pkName = null;
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                pkName = field.getName();
                break;
            }
        }
        if (pkName == null && type.getSuperclass() != null) {
            pkName = findFieldsForPK(type.getSuperclass());
        }
        return pkName;
    }

    /**
     * Devuelve el nombre del método que tiene al anotación @Id. En el caso de que ningún método de la clase tenga esta
     * anotación devolverá null.
     */
    private static String findMethodsForPK(final Class< ? > aType) {
        String pkName = null;
        Method[] methods = aType.getDeclaredMethods();
        for (Method method : methods) {
            Id id = method.getAnnotation(Id.class);
            if (id != null) {
                pkName = method.getName().substring(4);
                pkName = method.getName().substring(3, 4).toLowerCase() + pkName;
                break;
            }
        }
        if (pkName == null && aType.getSuperclass() != null) {
            pkName = findMethodsForPK(aType.getSuperclass());
        }
        return pkName;
    }

    /**
     * Obtiene el <code>Field</code> de la clase a la que corresponde el objeto y que coincide con el nombre indicado.
     * Para obtener el <code>Field</code> se consultará tanto la clase del objeto como sus super-clases.
     */
    private static Field getDeclaredField(final Object object, final String name) throws NoSuchFieldException {
        Field field = null;
        Class< ? > clazz = object.getClass();
        do {
            try {
                field = clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        } while (field == null & clazz != null);

        if (field == null) {
            throw new NoSuchFieldException();
        }

        return field;
    }
}
