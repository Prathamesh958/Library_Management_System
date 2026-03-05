import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import '../App.css'; // Reuse App.css for simple styling

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/login', { email, password });
            const { jwt, role, userId } = response.data;
            console.log(response)
            localStorage.setItem('token', jwt);
            localStorage.setItem('role', role);
            localStorage.setItem('userId', userId);

            if (role === 'LIBRARIAN') {
                navigate('/librarian');
            } else if (role === 'STUDENT') {
                navigate('/student');
            } else {
                setError('Unknown role');
            }
        } catch (err) {
            setError('Invalid credentials');
            console.error(err);
        }
    };

    return (
        <div className="container">
            <div className="login-box">
                <h2>SmartLib Login</h2>
                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>Email:</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Password:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    {error && <p className="error">{error}</p>}
                    <button type="submit">Login</button>
                </form>
            </div>
        </div>
    );
};

export default Login;
