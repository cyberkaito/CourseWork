using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PolyphoniaWeb.Models;

namespace PolyphoniaAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ClientTypesController : ControllerBase
    {
        private readonly PolyphoniaDatabaseContext _context;

        public ClientTypesController(PolyphoniaDatabaseContext context)
        {
            _context = context;
        }

        // GET: api/ClientTypes
        [HttpGet]
        public async Task<ActionResult<IEnumerable<ClientType>>> GetClientTypes()
        {
          if (_context.ClientTypes == null)
          {
              return NotFound();
          }
            return await _context.ClientTypes.ToListAsync();
        }

        // GET: api/ClientTypes/5
        [HttpGet("{id}")]
        public async Task<ActionResult<ClientType>> GetClientType(int? id)
        {
          if (_context.ClientTypes == null)
          {
              return NotFound();
          }
            var clientType = await _context.ClientTypes.FindAsync(id);

            if (clientType == null)
            {
                return NotFound();
            }

            return clientType;
        }

        // PUT: api/ClientTypes/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutClientType(int? id, ClientType clientType)
        {
            if (id != clientType.IdClientType)
            {
                return BadRequest();
            }

            _context.Entry(clientType).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClientTypeExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/ClientTypes
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<ClientType>> PostClientType(ClientType clientType)
        {
            clientType.IdClientType = null;
            if (_context.ClientTypes == null)
          {
              return Problem("Entity set 'PolyphoniaDatabaseContext.ClientTypes'  is null.");
          }
            _context.ClientTypes.Add(clientType);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetClientType", new { id = clientType.IdClientType }, clientType);
        }

        // DELETE: api/ClientTypes/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteClientType(int? id)
        {
            if (_context.ClientTypes == null)
            {
                return NotFound();
            }
            var clientType = await _context.ClientTypes.FindAsync(id);
            if (clientType == null)
            {
                return NotFound();
            }

            _context.ClientTypes.Remove(clientType);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool ClientTypeExists(int? id)
        {
            return (_context.ClientTypes?.Any(e => e.IdClientType == id)).GetValueOrDefault();
        }
    }
}
