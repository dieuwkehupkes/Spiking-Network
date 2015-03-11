import org.junit.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/*
import org.junit.validator.*;
import org.junit.*;
import org.junit.runners.*;
import org.junit.internal.requests.*;
import org.junit.internal.matchers.*;
import org.junit.internal.runners.model.*;
import org.junit.experimental.runners.*;
import org.junit.runners.model.*;
import org.junit.runner.notification.*;
import org.junit.experimental.categories.*;
import junit.framework.*;
import org.junit.internal.runners.rules.*;
import org.junit.internal.runners.*;
import junit.extensions.*;
import org.junit.experimental.theories.suppliers.*;
import org.junit.runner.*;
import org.junit.experimental.max.*;
import junit.textui.*;
import org.junit.matchers.*;
import org.junit.experimental.theories.internal.*;
import org.junit.runners.parameterized.*;
import junit.runner.*;
import org.junit.internal.runners.statements.*;
import org.junit.experimental.*;
import org.junit.experimental.results.*;
import org.junit.rules.*;
import org.junit.experimental.theories.*;
import org.junit.internal.*;
import org.junit.runner.manipulation.*;
import org.junit.internal.builders.*;
*/

abstract class UnitTest {

  public void testAll() throws IllegalAccessException, InvocationTargetException {
    /**
     * Execute all methods available in
     * the test class.
     */
    // import java.lang.reflect.method
    Method[] methods = this.getClass().getMethods();
    for (Method m : methods) {
      m.invoke(null);
    }
  }
}
