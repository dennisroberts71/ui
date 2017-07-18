package org.iplantc.de.theme.base.client.teams;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.core.client.GWT;

/**
 * The default appearance that will be used for the Teams view
 * @author aramsey
 */
public class TeamsViewDefaultAppearance implements TeamsView.TeamsViewAppearance {

    private IplantDisplayStrings iplantDisplayStrings;
    private IplantResources iplantResources;
    private TeamsDisplayStrings displayStrings;

    public TeamsViewDefaultAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<TeamsDisplayStrings> create(TeamsDisplayStrings.class));
    }

    public TeamsViewDefaultAppearance(IplantDisplayStrings iplantDisplayStrings,
                                      IplantResources iplantResources,
                                      TeamsDisplayStrings displayStrings) {

        this.iplantDisplayStrings = iplantDisplayStrings;
        this.iplantResources = iplantResources;
        this.displayStrings = displayStrings;
    }

    @Override
    public String teamsMenu() {
        return displayStrings.teamsMenu();
    }

    @Override
    public String createNewTeam() {
        return displayStrings.createNewTeam();
    }

    @Override
    public String manageTeam() {
        return displayStrings.manageTeam();
    }

    @Override
    public String leaveTeam() {
        return displayStrings.leaveTeam();
    }

    @Override
    public int nameColumnWidth() {
        return 300;
    }

    @Override
    public String nameColumnLabel() {
        return iplantDisplayStrings.name();
    }

    @Override
    public int descColumnWidth() {
        return 500;
    }

    @Override
    public String descColumnLabel() {
        return iplantDisplayStrings.description();
    }

    @Override
    public int infoColWidth() {
        return 20;
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String teamNameLabel() {
        return displayStrings.teamNameLabel();
    }

    @Override
    public String teamDescLabel() {
        return displayStrings.teamDescLabel();
    }

    @Override
    public int teamDetailsWidth() {
        return 500;
    }

    @Override
    public int teamDetailsHeight() {
        return 500;
    }

    @Override
    public String detailsHeading(Group group) {
        return displayStrings.detailsHeading(group.getExtension());
    }

    @Override
    public String membersLabel() {
        return displayStrings.membersLabel();
    }

    @Override
    public String detailsGridEmptyText() {
        return displayStrings.detailsGridEmptyText();
    }

    @Override
    public int editTeamWidth() {
        return 500;
    }

    @Override
    public int editTeamHeight() {
        return 700;
    }

    @Override
    public String editTeamHeading(Group group) {
        if (group == null) {
            return displayStrings.createNewTeam();
        } else {
            return displayStrings.editTeamHeader(group.getSubjectDisplayName());
        }
    }
}
