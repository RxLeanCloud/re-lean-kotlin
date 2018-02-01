package rx.leancloud.internal;

import rx.leancloud.core.RxAVObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class AVObjectSubclassingController {
    private final Object mutex = new Object();
    private final Map<String, Constructor<? extends RxAVObject>> registeredSubclasses = new HashMap<>();

    public String getClassName(Class<? extends RxAVObject> clazz) {
        AVClassName info = clazz.getAnnotation(AVClassName.class);
        if (info == null) {
            throw new IllegalArgumentException("No ParseClassName annotation provided on " + clazz);
        }
        return info.value();
    }

    public boolean isSubclassValid(String className, Class<? extends RxAVObject> clazz) {
        Constructor<? extends RxAVObject> constructor = null;

        synchronized (mutex) {
            constructor = registeredSubclasses.get(className);
        }

        return constructor == null
                ? clazz == RxAVObject.class
                : constructor.getDeclaringClass() == clazz;
    }

    public void registerSubclass(Class<? extends RxAVObject> clazz) {
        if (!RxAVObject.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Cannot register a type that is not a subclass of RxAVObject");
        }

        String className = getClassName(clazz);
        Constructor<? extends RxAVObject> previousConstructor = null;

        synchronized (mutex) {
            previousConstructor = registeredSubclasses.get(className);
            if (previousConstructor != null) {
                Class<? extends RxAVObject> previousClass = previousConstructor.getDeclaringClass();
                if (clazz.isAssignableFrom(previousClass)) {
                    // Previous subclass is more specific or equal to the current type, do nothing.
                    return;
                } else if (previousClass.isAssignableFrom(clazz)) {
                    // Previous subclass is parent of new child subclass, fallthrough and actually
                    // register this class.
                    /* Do nothing */
                } else {
                    throw new IllegalArgumentException(
                            "Tried to register both " + previousClass.getName() + " and " + clazz.getName() +
                                    " as the RxAVObject subclass of " + className + ". " + "Cannot determine the right " +
                                    "class to use because neither inherits from the other."
                    );
                }
            }

            try {
                registeredSubclasses.put(className, getConstructor(clazz));
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(
                        "Cannot register a type that does not implement the default constructor!"
                );
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException(
                        "Default constructor for " + clazz + " is not accessible."
                );
            }
        }

        if (previousConstructor != null) {
            // NOTE: Perform this outside of the mutex, to prevent any potential deadlocks.
//            if (className.equals(getClassName(RxAVUser.class))) {
//                //RxAVUser.getCurrentUserController().clearFromMemory();
//            } else if (className.equals(getClassName(ParseInstallation.class))) {
//                RxAVInstallation.getCurrentInstallationController().clearFromMemory();
//            }
        }
    }

    public void unregisterSubclass(Class<? extends RxAVObject> clazz) {
        String className = getClassName(clazz);

        synchronized (mutex) {
            registeredSubclasses.remove(className);
        }
    }

    public RxAVObject newInstance(String className) {
        Constructor<? extends RxAVObject> constructor = null;

        synchronized (mutex) {
            constructor = registeredSubclasses.get(className);
        }

        try {
            return constructor != null
                    ? constructor.newInstance()
                    : new RxAVObject(className);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of subclass.", e);
        }
    }

    private static Constructor<? extends RxAVObject> getConstructor(Class<? extends RxAVObject> clazz) throws NoSuchMethodException, IllegalAccessException {
        Constructor<? extends RxAVObject> constructor = clazz.getDeclaredConstructor();
        if (constructor == null) {
            throw new NoSuchMethodException();
        }
        int modifiers = constructor.getModifiers();
        if (Modifier.isPublic(modifiers) || (clazz.getPackage().getName().equals("rx.leancloud") &&
                !(Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers)))) {
            return constructor;
        }
        throw new IllegalAccessException();
    }
}
