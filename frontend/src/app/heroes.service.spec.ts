import { TestBed } from '@angular/core/testing';

import { HeroMage, HeroPaladin, HeroWarrior } from './heroes.service';

describe('HeroMage', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HeroMage = TestBed.get(HeroMage);
    expect(service).toBeTruthy();
  });
});

describe('HeroPaladin', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HeroPaladin = TestBed.get(HeroPaladin);
    expect(service).toBeTruthy();
  });
});

describe('HeroWarrior', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HeroWarrior = TestBed.get(HeroWarrior);
    expect(service).toBeTruthy();
  });
});
