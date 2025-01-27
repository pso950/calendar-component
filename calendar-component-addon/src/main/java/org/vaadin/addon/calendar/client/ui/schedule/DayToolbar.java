/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.addon.calendar.client.ui.schedule;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.vaadin.addon.calendar.client.DateConstants;
import org.vaadin.addon.calendar.client.ui.VCalendar;

import java.util.Date;

/**
 *
 * @since 7.1
 * @author Vaadin Ltd.
 *
 */
public class DayToolbar extends HorizontalPanel implements ClickHandler {

    private int width = 0;
    public static final int MARGINLEFT = 50;
    public static final int MARGINRIGHT = 15;
    protected Button backLabel;
    protected Button nextLabel;
    private boolean verticalSized;
    private boolean horizontalSized;
    private VCalendar calendar;

    public DayToolbar(VCalendar vcalendar) {
        calendar = vcalendar;

        setStylePrimaryName("v-calendar-header-week");

        backLabel = new Button();
        backLabel.setStylePrimaryName("v-calendar-back");
        backLabel.addClickHandler(this);

        nextLabel = new Button();
        nextLabel.addClickHandler(this);
        nextLabel.setStylePrimaryName("v-calendar-next");

        setBorderWidth(0);
        setSpacing(0);
    }

    public void setWidthPX(int width) {
        this.width = width - MARGINLEFT - MARGINRIGHT;
        // super.setWidth(this.width + "px");
        if (getWidgetCount() == 0) {
            return;
        }
        updateCellWidths();
    }

    public void updateCellWidths() {
        int count = getWidgetCount();
        if (count > 0) {
            setCellWidth(backLabel, MARGINLEFT + "px");
            setCellWidth(nextLabel, MARGINRIGHT + "px");
            setCellHorizontalAlignment(backLabel, ALIGN_RIGHT);
            setCellHorizontalAlignment(nextLabel, ALIGN_LEFT);
            int cellw = width / (count - 2);
            if (cellw > 0) {
                int[] cellWidths = VCalendar.distributeSize(width, count - 2,
                        0);
                for (int i = 1; i < count - 1; i++) {
                    Widget widget = getWidget(i);
                    // if (remain > 0) {
                    // setCellWidth(widget, cellw2 + "px");
                    // remain--;
                    // } else {
                    // setCellWidth(widget, cellw + "px");
                    // }
                    setCellWidth(widget, cellWidths[i - 1] + "px");
                    widget.setWidth(cellWidths[i - 1] + "px");
                }
            }
        }
    }

    public void add(String dayName, final Date date, String localized_date_format, String extraClass, boolean isWeekendDay) {

        HTML l = new HTML(("<span>" + dayName + "</span> " + localized_date_format).trim());
        l.setStylePrimaryName("v-calendar-header-day");

        if (extraClass != null) {
            l.addStyleDependentName(extraClass);
        }

        if (isWeekendDay) {
            l.addStyleDependentName("weekend");
        }

        if (verticalSized) {
            l.addStyleDependentName("Vsized");
        }
        if (horizontalSized) {
            l.addStyleDependentName("Hsized");
        }

        l.addClickHandler(ce -> {
            if (calendar.getDateClickListener() != null) {
                calendar.getDateClickListener().dateClick(DateConstants.toRPCDate(date));
            }
        });

        add(l);
    }

    public void addBackButton() {
        if (!calendar.isBackwardNavigationEnabled()) {
            nextLabel.getElement().getStyle().setHeight(0, Unit.PX);
        }
        add(backLabel);
    }

    public void addNextButton() {
        if (!calendar.isForwardNavigationEnabled()) {
            backLabel.getElement().getStyle().setHeight(0, Unit.PX);
        }
        add(nextLabel);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (!calendar.isDisabled()) {
            if (event.getSource() == nextLabel) {
                if (calendar.getForwardListener() != null) {
                    calendar.getForwardListener().forward();
                }
            } else if (event.getSource() == backLabel) {
                if (calendar.getBackwardListener() != null) {
                    calendar.getBackwardListener().backward();
                }
            }
        }
    }

    public void setVerticalSized(boolean sized) {
        verticalSized = sized;
        updateDayLabelSizedStyleNames();
    }

    public void setHorizontalSized(boolean sized) {
        horizontalSized = sized;
        updateDayLabelSizedStyleNames();
    }

    private void updateDayLabelSizedStyleNames() {
        for (Widget widget : this) {
            updateWidgetSizedStyleName(widget);
        }
    }

    private void updateWidgetSizedStyleName(Widget w) {
        if (verticalSized) {
            w.addStyleDependentName("Vsized");
        } else {
            w.removeStyleDependentName("VSized");
        }
        if (horizontalSized) {
            w.addStyleDependentName("Hsized");
        } else {
            w.removeStyleDependentName("HSized");
        }
    }
}
