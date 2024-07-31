package com.example.worldskillsapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Cadastro : AppCompatActivity() {
    private lateinit var textoSenha1: EditText
    private lateinit var textoSenha2: EditText
    private lateinit var imagemMostrarSenha1: ImageView
    private lateinit var imagemMostrarSenha2: ImageView
    private lateinit var usernameField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText

    private var senhaVisivel1: Boolean = false
    private var senhaVisivel2: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Inicializa as views
        usernameField = findViewById(R.id.nomeUsuario)
        passwordField = findViewById(R.id.caixinhaSenhaCadastro)
        confirmPasswordField = findViewById(R.id.conferirSenhaCadastro)
        textoSenha1 = passwordField
        textoSenha2 = confirmPasswordField
        imagemMostrarSenha1 = findViewById(R.id.senha1)
        imagemMostrarSenha2 = findViewById(R.id.senha2)

        // Configura a cor da barra de status e oculta a ActionBar
        window.statusBarColor = Color.parseColor("#3b2a93")
        supportActionBar?.hide()

        // Configura o botão de cadastro
        val botao: Button = findViewById(R.id.cadastrar)
        botao.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                showErrorMessage("Todos os campos são obrigatórios!")
            } else if (password != confirmPassword) {
                showErrorMessage("As senhas não coincidem!")
            } else if (!isPasswordValid(password)) {
                showErrorMessage("A senha deve ter no mínimo 6 caracteres, conter pelo menos uma letra maiúscula, 3 números e um caractere especial.")
            } else {
                // Salva os dados localmente
                saveUserData(username, password)
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // Configura os listeners para alternar a visibilidade das senhas
        imagemMostrarSenha1.setOnClickListener {
            senhaVisivel1 = !senhaVisivel1
            alternarVisibilidadeSenha(textoSenha1, imagemMostrarSenha1, senhaVisivel1)
        }

        imagemMostrarSenha2.setOnClickListener {
            senhaVisivel2 = !senhaVisivel2
            alternarVisibilidadeSenha(textoSenha2, imagemMostrarSenha2, senhaVisivel2)
        }
    }

    // Exibe mensagem de erro
    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Salva os dados do usuário localmente
    private fun saveUserData(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Armazena os dados do usuário
        editor.putString("username", username)
        editor.putString("password", password)

        // Confirma a gravação dos dados
        editor.apply()
    }

    // Valida os requisitos da senha
    private fun isPasswordValid(password: String): Boolean {
        if (password.length < 6) return false
        if (!password.any { it.isUpperCase() }) return false
        if (password.count { it.isDigit() } < 3) return false
        if (!password.any { it.isLetter() }) return false
        if (!password.any { !it.isLetterOrDigit() }) return false

        return true
    }

    // Alterna a visibilidade da senha
    private fun alternarVisibilidadeSenha(textoSenha: EditText, imagemMostrarSenha: ImageView, senhaVisivel: Boolean) {
        if (senhaVisivel) {
            // Mostrar senha
            textoSenha.transformationMethod = HideReturnsTransformationMethod.getInstance()
            imagemMostrarSenha.setImageResource(R.drawable.olho_open)
        } else {
            // Esconder senha
            textoSenha.transformationMethod = PasswordTransformationMethod.getInstance()
            imagemMostrarSenha.setImageResource(R.drawable.olho_close)
        }

        // Move o cursor para o final do texto
        textoSenha.setSelection(textoSenha.text.length)
    }
}
