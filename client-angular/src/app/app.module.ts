import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { GameComponent } from './game/game.component';
import { PlayerComponent } from './player/player.component';
import { HeroComponent } from './hero/hero.component';
import { ConstantesComponent } from './constantes/constantes.component';
import { CardComponent } from './card/card.component';
import { EffectsComponent } from './effects/effects.component';
import { EntityComponent } from './entity/entity.component';

@NgModule({
  declarations: [
    AppComponent,
    GameComponent,
    PlayerComponent,
    HeroComponent,
    ConstantesComponent,
    CardComponent,
    EffectsComponent,
    EntityComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
