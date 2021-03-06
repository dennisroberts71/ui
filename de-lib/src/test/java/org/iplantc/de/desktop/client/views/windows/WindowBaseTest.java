package org.iplantc.de.desktop.client.views.windows;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.commons.client.views.window.configs.WindowConfig;

import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.button.ToolButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GxtMockitoTestRunner.class)
public class WindowBaseTest {

    private WindowBase uut;

    @Before public void setup() {
        uut = new WindowBase() {
            @Override
            public String getWindowType() {
                return null;
            }

            @Override
            public FastMap<String> getAdditionalWindowStates() {
                return null;
            }

            @Override
            public WindowConfig getWindowConfig() {
                return null;
            }
        };
    }

    @Test public void minimizeFlagSetToFalseWhenWindowShown(){
        uut.minimized = true;

        uut.onShow();
        assertFalse(uut.minimized);
    }

    @Test public void minimizeFlagSetToFalseWhenMaximized(){
        uut.minimized = true;
        uut.isMaximizable = true;
        uut.btnRestore = mock(ToolButton.class);
        // To cause construction of Resizable object
        uut.getResizable();

        uut.setMaximized(true);
        assertFalse(uut.minimized);
    }

    @Test public void testSnapLeft() {
        final XElement mock = mock(XElement.class);
        final Rectangle mockRectangle = mock(Rectangle.class);
        // Test values are prime
        final int testValueX_half = 11;
        final int testValueWidth_half = 3;

        final int testValueX = testValueX_half*2;
        final int testValueY = 17;
        final int testValueWidth = testValueWidth_half*2;
        final int testValueHeight = 5;
        when(mockRectangle.getX()).thenReturn(testValueX);
        when(mockRectangle.getY()).thenReturn(testValueY);
        when(mockRectangle.getWidth()).thenReturn(testValueWidth);
        when(mockRectangle.getHeight()).thenReturn(testValueHeight);
        when(mock.getBounds()).thenReturn(mockRectangle);
        WindowBase uutSpy = spy(uut);
        uutSpy.doSnapLeft(mock);

        // Verify that page position is set to element's given X and Y
        verify(uutSpy).setPagePosition(eq(testValueX), eq(testValueY));
        verify(uutSpy).setPixelSize(eq(testValueWidth_half), eq(testValueHeight));
    }

    @Test public void testSnapRight() {
        final XElement mock = mock(XElement.class);
        final Rectangle mockRectangle = mock(Rectangle.class);
        // Test values are prime
        final int testValueX_half = 11;
        final int testValueWidth_half = 3;

        final int testValueX = testValueX_half*2;
        final int testValueY = 17;
        final int testValueWidth = testValueWidth_half*2;
        final int testValueHeight = 5;
        when(mockRectangle.getX()).thenReturn(testValueX);
        when(mockRectangle.getY()).thenReturn(testValueY);
        when(mockRectangle.getWidth()).thenReturn(testValueWidth);
        when(mockRectangle.getHeight()).thenReturn(testValueHeight);
        when(mock.getBounds()).thenReturn(mockRectangle);
        WindowBase uutSpy = spy(uut);
        uutSpy.doSnapRight(mock);

        // Verify that page position is set to half of element's width
        verify(uutSpy).setPagePosition(eq(testValueWidth_half), eq(testValueY));
        verify(uutSpy).setPixelSize(eq(testValueWidth_half), eq(testValueHeight));
    }
}
