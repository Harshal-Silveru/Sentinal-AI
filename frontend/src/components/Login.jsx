import React, { useState } from 'react';
import axios from 'axios';
import { Lock, User } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function Login({ setToken }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // Assuming user-service is running on 8081
      const res = await axios.post('http://localhost:8081/api/auth/login', { email, password });
      const token = res.data.token;
      localStorage.setItem('token', token);
      setToken(token);
      navigate('/');
    } catch (err) {
      setError('Invalid credentials or service unreachable');
    }
  };

  return (
    <div style={{ display: 'flex', height: '100vh', alignItems: 'center', justifyContent: 'center' }}>
      <div className="glass" style={{ padding: '3rem', width: '100%', maxWidth: '400px', textAlign: 'center' }}>
        <h2 style={{ marginBottom: '2rem', color: 'var(--primary)' }}>Secure DevSecOps Platform</h2>
        {error && <div style={{ color: 'var(--danger)', marginBottom: '1rem' }}>{error}</div>}
        <form onSubmit={handleLogin}>
          <div style={{ position: 'relative', marginBottom: '1rem' }}>
            <User style={{ position: 'absolute', left: '12px', top: '12px', color: 'var(--text-muted)' }} size={20} />
            <input 
              type="email" 
              className="input-field" 
              placeholder="Email" 
              style={{ paddingLeft: '40px' }} 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required 
            />
          </div>
          <div style={{ position: 'relative', marginBottom: '2rem' }}>
            <Lock style={{ position: 'absolute', left: '12px', top: '12px', color: 'var(--text-muted)' }} size={20} />
            <input 
              type="password" 
              className="input-field" 
              placeholder="Password" 
              style={{ paddingLeft: '40px' }} 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required 
            />
          </div>
          <button type="submit" className="btn-primary" style={{ width: '100%' }}>
            Login
          </button>
        </form>
      </div>
    </div>
  );
}
