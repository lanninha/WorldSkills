package com.example.worldskillsapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class Login : AppCompatActivity() {
    private lateinit var textoSenha: EditText
    private lateinit var imagemMostrarSenha: ImageView
    private lateinit var botaoRegistro: Button
    private lateinit var botaoLogin: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private var attemptCount = 0
    private var senhaVisivel: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configurações iniciais da Activity
        window.statusBarColor = Color.parseColor("#3b2a93")
        supportActionBar?.hide()

        // Inicializa as views
        textoSenha = findViewById(R.id.caixinhaSenha)
        imagemMostrarSenha = findViewById(R.id.olhoAberto)
        botaoRegistro = findViewById(R.id.botaoCadastro)
        botaoLogin = findViewById(R.id.botaoEntrar)
        username = findViewById(R.id.caixinhaUser)
        password = findViewById(R.id.caixinhaSenha)

        // Configura o botão de registro
        botaoRegistro.setOnClickListener {
            val intent = Intent(this, Cadastro::class.java)
            startActivity(intent)
        }

        // Configura o botão de login
        botaoLogin.setOnClickListener {
            val nome = username.text.toString()
            val senha = password.text.toString()

            if (validacaoLogin(nome, senha)) {
                val intent = Intent(this, Home::class.java)
                intent.putExtra("USERNAME", nome)
                startActivity(intent)
                finish()
            } else {
                attemptCount++
                if (attemptCount >= 5) {
                    bloquearMensagem()
                    disableInputs()
                    Handler(Looper.getMainLooper()).postDelayed({
                        enableInputs()
                    }, 10000)
                } else {
                    showErrorMessage()
                    highlightFields()
                }
            }
        }

        // Configura o botão para mostrar/esconder senha
        imagemMostrarSenha.setOnClickListener {
            senhaVisivel = !senhaVisivel
            visibilidadeSenha()
        }
    }

    // Valida o login
    private fun validacaoLogin(nome: String, senha: String): Boolean {
        val preferencia = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val storedUsername = preferencia.getString("username", null)
        val storedPassword = preferencia.getString("password", null)

        return nome == storedUsername && senha == storedPassword
    }

    // Exibe mensagem de erro
    private fun showErrorMessage() {
        Toast.makeText(this, "Usuário/Senha Inválidos!", Toast.LENGTH_SHORT).show()
    }

    // Exibe mensagem de bloqueio
    private fun bloquearMensagem() {
        Toast.makeText(this, "Login bloqueado, aguarde 10s!", Toast.LENGTH_SHORT).show()
    }

    // Destaca os campos
    private fun highlightFields() {
        username.background.setTint(ContextCompat.getColor(this, R.color.vermelho))
        password.background.setTint(ContextCompat.getColor(this, R.color.vermelho))
    }

    // Desativa os campos e botões
    private fun disableInputs() {
        botaoLogin.isEnabled = false
        botaoRegistro.isEnabled = false
        username.isEnabled = false
        password.isEnabled = false
    }

    // Ativa os campos e botões
    private fun enableInputs() {
        botaoLogin.isEnabled = true
        botaoRegistro.isEnabled = true
        username.isEnabled = true
        password.isEnabled = true
    }

    // Alterna a visibilidade da senha
    private fun visibilidadeSenha() {
        if (senhaVisivel) {
            password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imagemMostrarSenha.setImageResource(R.drawable.olho_open)
        } else {
            password.transformationMethod = PasswordTransformationMethod.getInstance()
            imagemMostrarSenha.setImageResource(R.drawable.olho_close)
        }

        // Move o cursor para o final do texto
        password.setSelection(password.text.length)
    }
}
