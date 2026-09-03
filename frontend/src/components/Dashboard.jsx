import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Shield, FileText, CheckCircle, Activity, LogOut } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

export default function Dashboard({ token }) {
  const [scans, setScans] = useState([]);
  const [analyses, setAnalyses] = useState([]);
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.reload();
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const config = { headers: { Authorization: `Bearer ${token}` } };
        // Fetch scans from project-service
        const scansRes = await axios.get('http://localhost:8082/api/scans', config);
        setScans(scansRes.data);
        
        // Fetch analyses from ai-analysis-service
        const analysesRes = await axios.get('http://localhost:8083/api/analyses', config);
        setAnalyses(analysesRes.data);
      } catch (err) {
        console.error("Failed to fetch dashboard data", err);
        if (err.response && err.response.status === 401) {
          handleLogout();
        }
      }
    };
    fetchData();
  }, [token]);

  return (
    <div className="container">
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '3rem' }}>
        <h1 style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Shield color="var(--primary)" size={32} /> DevSecOps Dashboard
        </h1>
        <button className="btn-primary" onClick={handleLogout} style={{ background: 'transparent', border: '1px solid var(--border-color)' }}>
          <LogOut size={18} /> Logout
        </button>
      </header>

      <div className="dashboard-grid" style={{ marginBottom: '3rem' }}>
        <div className="glass card" style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <div style={{ background: 'rgba(99, 102, 241, 0.2)', padding: '15px', borderRadius: '50%' }}>
            <Activity color="var(--primary)" size={24} />
          </div>
          <div>
            <h3 style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Total Scans</h3>
            <p style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{scans.length}</p>
          </div>
        </div>
        
        <div className="glass card" style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <div style={{ background: 'rgba(16, 185, 129, 0.2)', padding: '15px', borderRadius: '50%' }}>
            <CheckCircle color="var(--success)" size={24} />
          </div>
          <div>
            <h3 style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Analyzed Projects</h3>
            <p style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>{analyses.length}</p>
          </div>
        </div>

        <div className="glass card" style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
          <div style={{ background: 'rgba(245, 158, 11, 0.2)', padding: '15px', borderRadius: '50%' }}>
            <FileText color="var(--warning)" size={24} />
          </div>
          <div>
            <h3 style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Generated Reports</h3>
            <p style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>0</p>
          </div>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '2rem' }}>
        <div className="glass card">
          <h2 style={{ marginBottom: '1.5rem', fontSize: '1.2rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Activity size={20} color="var(--primary)" /> Recent Scans
          </h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {scans.slice(0, 5).map(scan => (
              <div key={scan.id} style={{ padding: '1rem', background: 'rgba(255,255,255,0.03)', borderRadius: '8px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div>
                  <div style={{ fontWeight: '500' }}>Project ID: {scan.projectId}</div>
                  <div style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>{new Date(scan.scanDate).toLocaleString()}</div>
                </div>
                <div style={{ color: scan.status === 'COMPLETED' ? 'var(--success)' : 'var(--warning)', fontWeight: 'bold', fontSize: '0.85rem' }}>
                  {scan.status}
                </div>
              </div>
            ))}
            {scans.length === 0 && <p style={{ color: 'var(--text-muted)' }}>No recent scans found.</p>}
          </div>
        </div>

        <div className="glass card">
          <h2 style={{ marginBottom: '1.5rem', fontSize: '1.2rem', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Shield size={20} color="var(--primary)" /> AI Analyses
          </h2>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {analyses.slice(0, 5).map(analysis => (
              <div key={analysis.id} style={{ padding: '1rem', background: 'rgba(255,255,255,0.03)', borderRadius: '8px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.5rem' }}>
                  <span style={{ fontWeight: '500' }}>Scan ID: {analysis.scanId}</span>
                  <span style={{ color: analysis.riskScore > 50 ? 'var(--danger)' : 'var(--warning)' }}>Risk: {analysis.riskScore}</span>
                </div>
                <p style={{ fontSize: '0.85rem', color: 'var(--text-muted)', display: '-webkit-box', WebkitLineClamp: 2, WebkitBoxOrient: 'vertical', overflow: 'hidden' }}>
                  {analysis.explanation}
                </p>
              </div>
            ))}
            {analyses.length === 0 && <p style={{ color: 'var(--text-muted)' }}>No analyses found.</p>}
          </div>
        </div>
      </div>
    </div>
  );
}
