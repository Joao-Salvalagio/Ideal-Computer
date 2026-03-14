import { ComponentFixture, TestBed } from '@angular/core/testing';

// 1. Importando o nome correto da classe que nós criamos
import { LoginComponent } from './login';

describe('LoginComponent', () => {
  // 2. Tipando as variáveis com o nome correto da classe
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent], // 3. Importando o componente no módulo de teste
    }).compileComponents();

    // 4. Criando a instância do componente
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
