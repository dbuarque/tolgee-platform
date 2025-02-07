import { ProjectDTO } from '../../../../webapp/src/service/response.types';

import {
  create4Translations,
  selectLangsInLocalstorage,
  translationsBeforeEach,
  visitTranslations,
} from '../../common/translations';
import { assertTooltip, gcy, selectInProjectMenu } from '../../common/shared';
import { stateColors } from '../../common/state';

describe('Translation states', () => {
  let project: ProjectDTO = null;

  beforeEach(() => {
    translationsBeforeEach()
      .then((p) => (project = p))
      .then(() => {
        create4Translations(project.id);
        visit();
      });
  });
  //
  // afterEach(() => {
  //   deleteProject(project.id);
  // });

  it('shows state indicator', () => {
    assertHasState('Cool translated text 1', 'Translated');
  });

  it('changes state to reviewed', () => {
    selectLangsInLocalstorage(project.id, ['en', 'cs']);
    const text = 'Studený přeložený text 2';
    setStateToReviewed(text);
    assertHasState(text, 'Reviewed');
    assertPercentageProjectList({ stateName: 'Reviewed', percentage: 25 });
  });

  it('changes state to need review', () => {
    setStateToReviewed('Cool translated text 2');
    getCell('Cool translated text 2')
      .trigger('mouseover')
      .findDcy('translation-state-button')
      .click();
    assertHasState('Cool translated text 2', 'Translated');
  });

  const setStateToReviewed = (translationText: string) => {
    getCell(translationText)
      .trigger('mouseover')
      .findDcy('translation-state-button')
      .click();
  };

  const getStateIndicator = (translationText: string) => {
    return getCell(translationText).findDcy('translations-state-indicator');
  };

  const getCell = (translationText: string) => {
    return cy
      .contains(translationText)
      .should('be.visible')
      .closestDcy('translations-table-cell');
  };

  const assertHasState = (
    translationText: string,
    stateName: keyof typeof stateColors
  ) => {
    getStateIndicator(translationText).trigger('mouseover');
    getStateIndicator(translationText)
      .find('div')
      .should('have.css', 'border-left', `4px solid ${stateColors[stateName]}`);
    assertTooltip(stateName);
    getStateIndicator(translationText).trigger('mouseout');
    visit();
    cy.contains(translationText).should('be.visible');
  };

  const assertPercentageProjectList = (props: {
    stateName: string;
    percentage: number;
  }) => {
    selectInProjectMenu('Projects');
    gcy('project-states-bar-legend').contains(
      `${props.stateName}: ${props.percentage}%`
    );
  };

  const visit = () => {
    visitTranslations(project.id);
  };
});
