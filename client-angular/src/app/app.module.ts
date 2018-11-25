import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GameComponent } from './game/game.component';
import { CardMinionComponent } from './card-minion/card-minion.component';
import { CardSpellComponent } from './card-spell/card-spell.component';
import { PlayerComponent } from './player/player.component';
import { HeroComponent } from './hero/hero.component';

@NgModule({
  declarations: [
    AppComponent,
    GameComponent,
    CardMinionComponent,
    CardSpellComponent,
    PlayerComponent,
    HeroComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
