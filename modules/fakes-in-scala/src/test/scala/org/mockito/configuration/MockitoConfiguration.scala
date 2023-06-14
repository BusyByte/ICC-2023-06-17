package org.mockito.configuration
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls
import org.mockito.stubbing.Answer

class MockitoConfiguration extends DefaultMockitoConfiguration {
  override def getDefaultAnswer(): Answer[AnyRef] = new ReturnsSmartNulls()
}
