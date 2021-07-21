package com.example.capstonesportapp.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.capstonesportapp.R
import com.example.capstonesportapp.core.data.Resource
import com.example.capstonesportapp.core.domain.model.Team
import com.example.capstonesportapp.core.ui.CountryAdapter
import com.example.capstonesportapp.core.ui.CountryAdapter.Companion.defaultCountryIdx
import com.example.capstonesportapp.core.ui.SportAdapter
import com.example.capstonesportapp.core.ui.SportAdapter.Companion.defaultSportIdx
import com.example.capstonesportapp.core.ui.TeamSearchAdapter
import com.example.capstonesportapp.core.ui.TeamSportAdapter
import com.example.capstonesportapp.databinding.FragmentHomeBinding
import com.example.capstonesportapp.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var defaultCategory: String = "Soccer"
    private var isAbleSearch: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val teamSportAdapter = TeamSportAdapter()
            val sportAdapter = SportAdapter()
            val countryAdapter = CountryAdapter()

            teamSportAdapter.onItemClick = { selectedTeamData -> teamItemClick(selectedTeamData) }

            sportAdapter.onItemClick = { selectedSportData, pos ->
                defaultSportIdx = pos
                sportAdapter.onRefreshAdapter()
                observeTeamAdapter(
                    teamSportAdapter,
                    countryAdapter,
                    selectedSportData.name,
                    defaultCountryIdx
                )
            }

            countryAdapter.onItemClick = { pos ->
                observeTeamAdapter(teamSportAdapter, countryAdapter, defaultCategory, pos)
            }

            observeCountryAdapter(countryAdapter, teamSportAdapter)
            observeSportAdapter(sportAdapter)
            homeViewModel.initSearchAdapter()
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val search: MenuItem? = menu.findItem(R.id.navSearch)
        val searchView = search?.actionView as SearchView

        searchView.queryHint = "Search sport... "
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val teamSportAdapter = TeamSportAdapter()
                val allTeam = ArrayList<String>()

                teamSportAdapter.onItemClick = {
                        selectedTeamData -> teamItemClick(selectedTeamData)
                }
                homeViewModel.teamAdapter.observe(viewLifecycleOwner, { teamData ->
                        teamData.data?.let {
                            it.map { theTeam ->
                                allTeam.add(theTeam.name)
                            }
                        }
                    }
                )

                homeViewModel.generateSearchAdapter(query, allTeam)
                observeSearchTeamAdapter(teamSportAdapter)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (isAbleSearch && newText.isNotEmpty()) {
                    lifecycleScope.launch {
                        homeViewModel.queryChannel.send(newText)
                    }
                    binding.incHomeHeader.autoCompleteSearch.visibility = View.VISIBLE
                    observeSearchAdapter(searchView)
                } else {
                    with(binding) {
                        incHomeHeader.headerRecyclerView.visibility = View.VISIBLE
                        incHomeEventSport.eventSportRecyclerView.visibility = View.VISIBLE
                        incHomeEventSport.txtEventSport.visibility = View.VISIBLE
                        incHomeHeader.autoCompleteSearch.visibility = View.GONE
                    }
                }
                return false
            }
        })

        searchView.setOnFocusChangeListener { view, isFocus ->
            if (isFocus) {
                view.visibility = View.VISIBLE
                isAbleSearch = true
            } else {
                view.visibility = View.GONE
                isAbleSearch = false
            }
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeCountryAdapter(
        countryAdapter: CountryAdapter,
        teamSportAdapter: TeamSportAdapter
    ) {
        with(binding) {
            homeViewModel.countryAdapter.observe(viewLifecycleOwner, { country ->
                if (country != null) {
                    when (country) {
                        is Resource.Loading -> incHomeHeader.headerProgressSportBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            countryAdapter.setData(country.data)
                            homeViewModel.generateSportAdapter(country.data)
                            observeTeamAdapter(
                                teamSportAdapter,
                                countryAdapter,
                                defaultCategory,
                                defaultCountryIdx
                            )

                            incHomeHeader.headerProgressSportBar.visibility = View.GONE
                        }
                        is Resource.Error -> {
                            incHomeHeader.headerProgressSportBar.visibility = View.GONE
                            incHomeFragmentError.root.visibility = View.VISIBLE
                        }
                    }
                }
            })

            incHomeHeader.headerRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            incHomeHeader.headerRecyclerView.adapter = countryAdapter
        }
    }

    private fun observeSportAdapter(sportAdapter: SportAdapter)
    {
        with(binding) {
            homeViewModel.sportAdapter.observe(viewLifecycleOwner, { sport ->
                if (sport != null) {
                    when (sport) {
                        is Resource.Loading -> incHomeEventSport.eventSportProgressBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            incHomeEventSport.eventSportProgressBar.visibility = View.GONE
                            sportAdapter.setData(sport.data)
                        }
                        is Resource.Error -> {
                            incHomeEventSport.eventSportProgressBar.visibility = View.GONE
                            incHomeFragmentError.root.visibility = View.VISIBLE
                        }
                    }
                }
            })

            incHomeEventSport.eventSportRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            incHomeEventSport.eventSportRecyclerView.adapter = sportAdapter
        }
    }

    private fun observeTeamAdapter(
        teamSportAdapter: TeamSportAdapter,
        countryAdapter: CountryAdapter,
        sportCategory: String,
        idx: Int
    )
    {
        defaultCategory = sportCategory
        defaultCountryIdx = idx

        countryAdapter.onRefreshAdapter()

        with(binding) {
            homeViewModel.teamSportAdapter[defaultCountryIdx].observe(viewLifecycleOwner, { team ->
                if (team != null) {
                    when (team) {
                        is Resource.Loading -> onLoadingSportTeam()
                        is Resource.Success -> {
                            val teamFilter = team.data?.filter { teamData ->
                                teamData.sportCategory == sportCategory
                            }
                            if (!teamFilter.isNullOrEmpty()) {
                                isAbleSearch = true

                                teamSportAdapter.setData(teamFilter)
                                incHomeHeader.txtTotalSportsCount.text =
                                    teamSportAdapter.itemCount.toString()

                                incHomeTeamVsTeam.root.visibility = View.VISIBLE
                                incHomeTeamVsTeam.teamRecyclerView.visibility = View.VISIBLE
                                incHomeFragmentEmpty.root.visibility = View.GONE
                                incHomeTeamVsTeam.teamProgressBar.visibility = View.GONE
                            } else {
                                incHomeTeamVsTeam.root.visibility = View.GONE
                                incHomeTeamVsTeam.teamProgressBar.visibility = View.GONE
                                incHomeFragmentEmpty.root.visibility = View.VISIBLE
                            }
                        }
                        is Resource.Error -> {
                            incHomeTeamVsTeam.teamProgressBar.visibility = View.GONE
                            incHomeFragmentError.root.visibility = View.VISIBLE
                        }
                    }
                }
            })

            incHomeTeamVsTeam.teamRecyclerView.layoutManager = GridLayoutManager(context, 2)
            incHomeTeamVsTeam.teamRecyclerView.adapter = teamSportAdapter

            homeViewModel.favoriteTeam.observe(viewLifecycleOwner, { teamData ->
                incHomeHeader.txtFavoriteCount.text = teamData.size.toString()
            })

            homeViewModel.seenTeam.observe(viewLifecycleOwner, { teamData ->
                incHomeHeader.txtDetailSeenCount.text = teamData.size.toString()
            })
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observeSearchAdapter(searchView: SearchView) {
        with(binding) {
            homeViewModel.searchResult.observe(viewLifecycleOwner, { team ->
                val teamName = arrayListOf<String>()

                if (team != null && team.isNotEmpty()) {
                    val adapter = TeamSearchAdapter()

                    team.map {
                        teamName.add(it.name)
                    }

                    adapter.setData(teamName)
                    adapter.onItemClick = { selectedAutoComplete ->
                        autoCompleteItemClick(selectedAutoComplete, searchView)
                    }

                    incHomeHeader.autoCompleteSearch.layoutManager = GridLayoutManager(context, 1)
                    incHomeHeader.autoCompleteSearch.adapter = adapter
                }
            })
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun observeSearchTeamAdapter(teamSportAdapter: TeamSportAdapter) {
        with(binding) {
            onQuerySubmit()

            homeViewModel.searchTeamAdapter[0].observe(viewLifecycleOwner, { team ->
                if (team != null) {
                    when (team) {
                        is Resource.Loading -> onLoadingSportTeam()
                        is Resource.Success -> {
                            teamSportAdapter.setData(team.data)

                            incHomeHeader.txtTotalSportsCount.text =
                                teamSportAdapter.itemCount.toString()
                            incHomeTeamVsTeam.root.visibility = View.VISIBLE
                            incHomeTeamVsTeam.teamRecyclerView.visibility = View.VISIBLE
                            incHomeFragmentEmpty.root.visibility = View.GONE
                            incHomeTeamVsTeam.teamProgressBar.visibility = View.GONE
                        }
                        is Resource.Error -> {
                            incHomeTeamVsTeam.teamProgressBar.visibility = View.GONE
                            incHomeFragmentError.root.visibility = View.VISIBLE
                        }
                    }

                    incHomeTeamVsTeam.teamRecyclerView.layoutManager = GridLayoutManager(context, 2)
                    incHomeTeamVsTeam.teamRecyclerView.adapter = teamSportAdapter
                }
            })
        }
    }

    private fun teamItemClick(selectedTeamData: Team) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.TEAM_DATA, selectedTeamData)
        startActivity(intent)
    }

    private fun autoCompleteItemClick(selectedText: String, searchView: SearchView) {
        searchView.setQuery(selectedText, true)
    }

    private fun onQuerySubmit() {
        with(binding) {
            incHomeHeader.autoCompleteSearch.visibility = View.GONE
            incHomeHeader.headerRecyclerView.visibility = View.GONE
            incHomeEventSport.eventSportRecyclerView.visibility = View.GONE
            incHomeEventSport.txtEventSport.visibility = View.GONE
        }
    }

    private fun onLoadingSportTeam() {
        with(binding) {
            incHomeTeamVsTeam.teamProgressBar.visibility = View.VISIBLE
            incHomeTeamVsTeam.teamRecyclerView.visibility = View.GONE
        }
    }
}
